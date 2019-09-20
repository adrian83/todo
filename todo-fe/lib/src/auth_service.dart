import 'dart:async';
import 'dart:convert';
// import 'dart:html';

import 'package:http/http.dart';

import 'user.dart';
import 'store.dart';
import '../generated_consts.dart';

class AuthService {
  static final _headers = {'Content-Type': 'application/json'};
  static const _authUrl = '/api/v1/auth/';

  final Client _http;
  final Store _store;

  AuthService(this._http, this._store);



  String _authApi() => backendHost + _authUrl;

  Future<void> register(Register register) async {
    try {
      print("in service");
      //final response = await _http.get(_authApi() + "register");
      final response = await _http.post(_authApi() + "register",
          headers: _headers, body: json.encode(register));
      print(response.body); 

      if(response.statusCode != 200 && response.statusCode != 201){
        throw ArgumentError("invalid register status"); 
      }
      
      return;
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<void> login(Login login) async {
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
  }

  Exception _handleError(dynamic e) {
    print(e); // for demo purposes only
    return Exception('Server error; cause: $e');
  }

}
