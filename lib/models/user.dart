// models/user.dart
class User {
  final String userId;
  final String address;
  final String email;
  final String firstName;
  final String lastName;

  User({
    required this.userId,
    required this.address,
    required this.email,
    required this.firstName,
    required this.lastName,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['userId'],
      address: json['address'],
      email: json['email'],
      firstName: json['firstName'],
      lastName: json['lastName'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'userId': userId,
      'address': address,
      'email': email,
      'firstName': firstName,
      'lastName': lastName,
    };
  }
}
