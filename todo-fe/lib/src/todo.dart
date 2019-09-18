class Todo {
  final int id;
  String text;

  Todo(this.id, this.text);

  factory Todo.fromJson(Map<String, dynamic> json){
    print("todo json $json");
return Todo(_toInt(json['id']), json['text']);
  }
      

  Map toJson() => {'id': id, 'text': text};
}

int _toInt(id) => id is int ? id : int.parse(id);
