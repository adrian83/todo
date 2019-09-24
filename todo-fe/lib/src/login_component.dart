import 'dart:convert';

import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';


import 'user.dart';
import 'auth_service.dart';
import 'event_bus.dart';
import 'route_paths.dart';
import 'error_component.dart';
import 'validation_component.dart';
import 'info_component.dart';
import 'form_component.dart';
import 'error.dart';
import 'store.dart';

@Component(
  selector: 'login',
  templateUrl: 'login_component.html',
  directives: [coreDirectives, ErrorComponent, ValidationComponent, InfoComponent],
    providers: [ClassProvider(AuthService), ClassProvider(EventBus)],
  pipes: [commonPipes],
)
class LoginComponent extends FormComponent {

  Router _router;
  AuthService _authService;
  EventBus _eventBus;
  Store _store;

  LoginComponent(this._authService, this._router, this._eventBus, this._store);

  void login(String email, String password) async {
    print("Email: $email, password: $password");
    var login = Login(email, password);

    _authService.login(login)
      .then((response){
        if(response.statusCode == 200 || response.statusCode == 201){
          print(response.body);

          var headers = response.headers;
          print("headers $headers");
          var authToken = headers['authorization'];

          print("1 $authToken");

          _store.storeAuthToken(authToken);

          _eventBus.onEvent(true);
          print("go to home, router $_router");
          _router.navigate(RoutePaths.dashboard.toUrl());
          print("go to home, router $_router");
        } else if(response.statusCode == 400) {
          print(response.body);
          Iterable l = json.decode(response.body);
          violations = l.map((j)=> ConstraintViolation.fromJson(j)).toList();
        } else {
          errorMsg = response.body != null ? response.body : "unknown error";
        }
      });
  }
}
