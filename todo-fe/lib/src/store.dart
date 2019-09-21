



class Store {

  static Map<String, String> _data = {};
  
  static String _tokenKey = "authToken"; 


  void storeAuthToken(String token) {
    _data[_tokenKey] = token;
  }

  String getAuthToken() {
    var token = _data[_tokenKey];
    print("AUTH TOKEN $token");
    return token;
  }

}