



class Store {

  static Map<String, String> _data = {};
  
  static String _tokenKey = "authToken"; 


  void storeAuthToken(String token) {
    _data[_tokenKey] = token;
  }

  String getAuthToken() {
    return _data[_tokenKey];
  }

}