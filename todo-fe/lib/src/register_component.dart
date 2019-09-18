import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';

import 'route_paths.dart';
import 'auth_service.dart';
import 'user.dart';

@Component(
  selector: 'register',
  templateUrl: 'register_component.html',
  directives: [coreDirectives],
  providers: [ClassProvider(AuthService)],
  pipes: [commonPipes],
)
class RegisterComponent implements OnInit {

  final Router _router;
  AuthService _authService;

  RegisterComponent(this._authService, this._router);

  void ngOnInit(){

  }

  void register(String email, String password, String repeatedPassword) async {
    print("Email: $email, password: $password, password2: $repeatedPassword");
    var register = Register(email, password, repeatedPassword);
    await _authService.register(register);
    //print(user.toJson());
  }

}
