import 'package:agroautomated/app_theme.dart';
import 'package:agroautomated/provider/theme_provider.dart';
import 'package:agroautomated/widgets/app_text_widget.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:agroautomated/widgets/app_appbar_widget_page.dart';
import 'package:firebase_auth/firebase_auth.dart';

class HomePage extends ConsumerStatefulWidget {
  HomePage({Key? key});

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends ConsumerState<HomePage> {
  final user = FirebaseAuth.instance.currentUser!;

  void singUserOut() {
    FirebaseAuth.instance.signOut();
  }

  @override
  Widget build(BuildContext context) {
    final appThemeState = ref.watch(appThemeStateNotifier);

    return Scaffold(
      appBar: const AppAppBar(),
      backgroundColor: appThemeState.isDarkModeEnabled
          ? AppTheme.darkTheme.dialogBackgroundColor
          : AppTheme.lightTheme.dialogBackgroundColor,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            IconButton(onPressed: singUserOut, icon: const Icon(Icons.logout)),
            AppText(text: "Logged as ${user.email!}"),
            SizedBox(height: 20),
            Expanded(
              child: _buildUserListWithPlants(), // Show plant list
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildUserListWithPlants() {
    return StreamBuilder(
      stream: FirebaseFirestore.instance.collection('plants').snapshots(),
      builder: (context, AsyncSnapshot<QuerySnapshot> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return CircularProgressIndicator();
        }
        if (snapshot.hasError) {
          return Text('Error: ${snapshot.error}');
        }
        if (!snapshot.hasData || snapshot.data!.docs.isEmpty) {
          return Text('No Plants Found');
        }

        return ListView(
          children: snapshot.data!.docs.map((DocumentSnapshot document) {
            Map<String, dynamic> data = document.data() as Map<String, dynamic>;

            return ListTile(
              title: AppText(text: data['title']),
              subtitle: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  AppText(text: 'Type: ${data['type']}'),
                  AppText(text: 'Location: ${data['location']}'),
                  AppText(text: 'Indoor: ${data['isIndoor']}'),
                  AppText(text: 'Notification: ${data['notification']}'),
                ],
              ),
              trailing: FutureBuilder(
                future: _getUser(data['userId']),
                builder:
                    (context, AsyncSnapshot<DocumentSnapshot> userSnapshot) {
                  if (userSnapshot.connectionState == ConnectionState.waiting) {
                    return CircularProgressIndicator();
                  }
                  if (userSnapshot.hasError) {
                    return Text('Error: ${userSnapshot.error}');
                  }
                  if (!userSnapshot.hasData) {
                    return Text('User not found');
                  }

                  Map<String, dynamic> userData =
                      userSnapshot.data!.data() as Map<String, dynamic>;

                  return AppText(
                      text: '${userData['firstName']} ${userData['lastName']}');
                },
              ),
            );
          }).toList(),
        );
      },
    );
  }

  Future<DocumentSnapshot> _getUser(String userId) async {
    return await FirebaseFirestore.instance
        .collection('users')
        .doc(userId)
        .get();
  }
}
