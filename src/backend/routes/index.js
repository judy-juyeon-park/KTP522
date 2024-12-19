var express = require('express');
var router = express.Router();

// AS18.서버에 MVC 패턴을 적용해 로직 간 분리를 명확히 유지 (View 생략)
router.use('/api/tales', require('./tales'));
router.use('/api/talesAI', require('./tales_ai'));

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
