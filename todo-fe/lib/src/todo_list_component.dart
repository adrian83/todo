import 'dart:convert';

import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';

import 'route_paths.dart';
import 'routes.dart';

import 'auth_service.dart';
import 'todo_service.dart';
import 'todo.dart';
import 'store.dart';
import 'error.dart';
import 'error_component.dart';
import 'validation_component.dart';
import 'info_component.dart';
import 'form_component.dart';


@Component(
  selector: 'todo-list',
  templateUrl: 'todo_list_component.html', 
  directives: [coreDirectives, routerDirectives, ErrorComponent, ValidationComponent, InfoComponent],
  exports: [RoutePaths, Routes],
  providers: [ClassProvider(Store), ClassProvider(TodoService)],
  pipes: [commonPipes],
)
class TodoListComponent extends FormComponent implements OnInit {

  final Router _router;
  Store _store;
  TodoService _todoService;

  List<Todo> todos = [];


  TodoListComponent(this._todoService, this._router, this._store);

  void ngOnInit() async {
    var authToken = _store.getAuthToken();
    //print("auth token: $authToken");
    print("test");
    todos = await _todoService.getAll(authToken);
    print("todos: $todos");
  }

  void create(String text) async {
    var authToken = _store.getAuthToken();
    _todoService.create(authToken, text)
      .then((response){
        if(response.statusCode == 200 || response.statusCode == 201){
          print(response.body);
          infoMsg = "Todo created";
          var todo = Todo.fromJson(json.decode(response.body));
          todos.insert(0, todo);
        } else if(response.statusCode == 400) {
          Iterable l = json.decode(response.body);
          violations = l.map((j)=> ConstraintViolation.fromJson(j)).toList();
        } else {
          errorMsg = response.body != null ? response.body : "unknown error";
        }
      });
    
  }

}


