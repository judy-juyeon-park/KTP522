var express = require('express');
var router = express.Router();

// AS18.서버에 MVC 패턴을 적용해 로직 간 분리를 명확히 유지 (View 생략)
/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
