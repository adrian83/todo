
import 'error.dart';

class FormComponent {

  List<ConstraintViolation> _violations = [];
  String _errorMsg;
  String _infoMsg;

  String get infoMsg => _infoMsg;

  void set infoMsg(String msg) {
    _infoMsg = msg;
  }

  Function get hideInfo => () => _infoMsg = "";


  String get errorMsg => _errorMsg;

  void set errorMsg(String msg) {
    _errorMsg = msg;
  }

  Function get hideError => () => _errorMsg = "";


  List<ConstraintViolation> get violations => _violations;

  void set violations(List<ConstraintViolation>  vs) {
    _violations = vs;
  }

  Function get hideConstraintViolation => () => _violations = [];

}