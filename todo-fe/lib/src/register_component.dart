import 'dart:convert';

import 'package:angular/angular.dart';

import 'auth_service.dart';
import 'user.dart';
import 'error.dart';
import 'error_component.dart';
import 'validation_component.dart';
import 'info_component.dart';
import 'form_component.dart';

@Component(
  selector: 'register',
  templateUrl: 'register_component.html',
  directives: [coreDirectives, ErrorComponent, ValidationComponent, InfoComponent],
  providers: [ClassProvider(AuthService)],
  pipes: [commonPipes],
)
class RegisterComponent extends FormComponent {

  AuthService _authService;

  RegisterComponent(this._authService);


  void register(String email, String password, String repeatedPassword) async {
    print("Email: $email, password: $password, password2: $repeatedPassword");
    var register = Register(email, password, repeatedPassword);

    _authService.register(register)
      .then((response){
        if(response.statusCode == 200 || response.statusCode == 201){
          print(response.body);
          infoMsg = "Successfully registered";
        } else if(response.statusCode == 400) {
          Iterable l = json.decode(response.body);
          violations = l.map((j)=> ConstraintViolation.fromJson(j)).toList();
        } else {
          errorMsg = response.body != null ? response.body : "unknown error";
        }
      });

/*
    try {
      print("in service");
      //final response = await _http.get(_authApi() + "register");
      final response = await _http.post(_authApi() + "login",
          headers: _headers, body: json.encode(login));
      print(response.body); 

      if(response.statusCode != 200 && response.statusCode != 201){
        throw ArgumentError("invalid login status"); 
      }

      var headers = response.headers;
      print("headers $headers");
      var authToken = headers['authorization'];

      print("1 $authToken");

      _store.storeAuthToken(authToken);

      return;
    } catch (e) {
      throw _handleError(e);
    }
*/

    
  }

}
