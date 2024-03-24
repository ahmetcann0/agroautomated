import 'package:agroautomated/app_theme.dart';
import 'package:agroautomated/models/plant.dart';
import 'package:agroautomated/provider/plant_provider.dart';
import 'package:agroautomated/provider/theme_provider.dart';
import 'package:agroautomated/provider/user_provider.dart';
import 'package:agroautomated/widgets/app_button_widget.dart';
import 'package:agroautomated/widgets/app_text_widget.dart';
import 'package:agroautomated/widgets/app_textfield_widget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:agroautomated/models/plant_types.dart';
import 'package:agroautomated/provider/plant_types_provider.dart';

final selectedTypeProvider = StateProvider<String>((ref) => 'Indoor');
final selectedPlantTypeProvider = StateProvider<String>((ref) => 'Buğday');

class PlantBottomSheet {
  static void show(BuildContext context) {
    showModalBottomSheet(
      context: context,
      builder: (BuildContext context) {
        return ProviderScope(
          child: PlantBottomSheetContent(),
        );
      },
    );
  }
}

class PlantBottomSheetContent extends ConsumerWidget {
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _locationController = TextEditingController();

  PlantBottomSheetContent({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedType2 = ref.watch(selectedTypeProvider);
    //final selectedType = ref.watch(selectedPlantTypeProvider);
    final appThemeState = ref.watch(appThemeStateNotifier);

    final AsyncValue<List<PlantType>> plantTypesAsyncValue =
        ref.watch(plantTypesProvider);

    return Container(
      color: appThemeState.isDarkModeEnabled
          ? AppTheme.darkTheme.dialogBackgroundColor
          : AppTheme.lightTheme.dialogBackgroundColor,
      padding: EdgeInsets.all(20.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          AppText(
            text: 'Add new plant',
            fontSize: 25.0,
            fontWeight: FontWeight.bold,
          ),
          SizedBox(height: 20),
          AppText(
            text: 'Title',
            fontSize: 16.0,
            fontWeight: FontWeight.bold,
          ),
          SizedBox(height: 10),
          AppTextField(
            controller: _titleController,
            hintText: 'Enter',
          ),
          SizedBox(height: 10),
          AppText(
            text: 'Location',
            fontSize: 16.0,
            fontWeight: FontWeight.bold,
          ),
          SizedBox(height: 10),
          AppTextField(
            controller: _locationController,
            hintText: 'Enter your plant location',
          ),
          SizedBox(height: 10),
          AppText(
            text: 'Plant Environment:',
            fontSize: 16.0,
            fontWeight: FontWeight.bold,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              Row(
                children: [
                  Radio(
                    activeColor: appThemeState.isDarkModeEnabled
                        ? Colors.black
                        : Colors.white,
                    value: 'Indoor',
                    groupValue: selectedType2,
                    onChanged: (value) {
                      _updateSelectedType(ref, value.toString());
                    },
                  ),
                  AppText(text: 'Indoor'),
                ],
              ),
              Row(
                children: [
                  Radio(
                    activeColor: appThemeState.isDarkModeEnabled
                        ? Colors.black
                        : Colors.white,
                    value: 'Outdoor',
                    groupValue: selectedType2,
                    onChanged: (value) {
                      _updateSelectedType(ref, value.toString());
                    },
                  ),
                  AppText(text: 'Outdoor'),
                ],
              )
            ],
          ),
          SizedBox(height: 10),
          AppText(
            text: 'Plant Type:',
            fontSize: 16.0,
            fontWeight: FontWeight.bold,
          ),
          plantTypesAsyncValue.when(
            data: (plantTypes) {
              return Expanded(
                child: CupertinoPicker(
                  backgroundColor: appThemeState.isDarkModeEnabled
                      ? AppTheme.darkTheme.dialogBackgroundColor
                      : AppTheme.lightTheme.dialogBackgroundColor,
                  itemExtent: 32.0,
                  onSelectedItemChanged: (int index) {
                    _updateSelectedPlantType(ref, plantTypes[index].name);
                  },
                  children: List.generate(
                    plantTypes.length,
                    (index) =>
                        AppText(text: plantTypes[index].name, fontSize: 20.0),
                  ),
                ),
              );
            },
            loading: () => Center(
              child: CircularProgressIndicator(),
            ),
            error: (error, stackTrace) => Center(
              child: Text('Error: $error'),
            ),
          ),
          SizedBox(height: 20),
          AppButton(
            onTap: () {
              _addPlant(context, ref);
            },
            text: ('Add Plant'),
          ),
        ],
      ),
    );
  }

  void _updateSelectedType(WidgetRef ref, String value) {
    ref.read(selectedTypeProvider.notifier).state = value;
  }

  void _updateSelectedPlantType(WidgetRef ref, String value) {
    ref.read(selectedPlantTypeProvider.notifier).state = value;
  }

  void _addPlant(BuildContext context, WidgetRef ref) {
    String title = _titleController.text;
    String location = _locationController.text;
    String type = ref.watch(selectedTypeProvider);
    String plantType = ref.watch(selectedPlantTypeProvider);
    final currentUser = ref.watch(userProvider.notifier).state;

    Plant newPlant = Plant(
      title: title,
      type: plantType,
      location: location,
      isIndoor: type == 'Indoor',
      userId: currentUser.uid,
    );

    ref.read(plantProvider).addPlant(newPlant);

    _titleController.clear();
    _locationController.clear();

    Navigator.of(context).pop();
  }
}
