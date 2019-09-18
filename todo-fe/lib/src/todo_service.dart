import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart';

import 'todo.dart';
import '../generated_consts.dart';

class TodoService {
  static final _headers = {'Content-Type': 'application/json'};
  static const _todosUrl = '/api/v1/todos'; 
  final Client _http;

  TodoService(this._http);

  String _todosApi() => backendHost + _todosUrl;

  Future<List<Todo>> getAll(String authToken) async {
    try {
      print("getAll");
      final headers = {'Authorization': authToken};
      final response = await _http.get(_todosApi(), headers: headers);
      print("all todos: ${response.body}");
      final todos = (_extractData(response) as List)
          .map((json) => Todo.fromJson(json))
          .toList();
      return todos;
    } catch (e) {
      throw _handleError(e);
    }
  }

  dynamic _extractData(Response resp) => json.decode(resp.body);

  Exception _handleError(dynamic e) {
    print(e); // for demo purposes only
    return Exception('Server error; cause: $e');
  }

  Future<Todo> get(int id) async {
    try {
      final response = await _http.get('${_todosApi()}/$id');
      return Todo.fromJson(_extractData(response));
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<Todo> create(String text) async {
    try {
      final response = await _http.post(_todosApi(),
          headers: _headers, body: json.encode({'text': text}));
      return Todo.fromJson(_extractData(response));
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<Todo> update(Todo todo) async {
    try {
      final url = '${_todosApi()}/${todo.id}';
      final response =
          await _http.put(url, headers: _headers, body: json.encode(todo));
      return Todo.fromJson(_extractData(response));
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<void> delete(int id) async {
    try {
      final url = '${_todosApi()}/$id';
      await _http.delete(url, headers: _headers);
    } catch (e) {
      throw _handleError(e);
    }
  }
}
