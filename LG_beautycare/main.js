var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var fs = require('fs');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.listen(3000, function () {
    console.log('Server launch...');
});

var connection = mysql.createConnection({
    host: "db-instance-algoitso.ckebjmyaqenk.us-east-2.rds.amazonaws.com",
    user: "admin",
    database: "example",
    password: "cocomong99",
    port: 3306
});

//---------------- multer ----------------
var multer = require('multer');
var storage = multer.diskStorage({
  destination: function(req, file, cb){
    cb(null, 'UserData/')
  },
  filename: function(req, file, cb){
    cb(null, file.originalname)
  }
});
var up_img = multer({storage:storage});
//---------------- multer ----------------

let {PythonShell} = require('python-shell');

// 모공 이미지 분석 요청
app.post('/:user_name/pore', up_img.single('img'), function(req, res){
	var user_name = req.params.user_name;
	var img_name = req.file.originalname;
	console.log('# Pore image analysis requrest arrive. (' + user_name + ' : ' + img_name + ')');
	var options = {
		mode : 'text',
		pythonPath:'/usr/bin/python3',
		pythonOptions:['-u'],
		args: [user_name, img_name]
	};
	PythonShell.run('pore.py', options, function(err){
		if(err) throw err;
		else {
			console.log('- Pore image analysis done.');
			var filepath = 'UserData/pore_' + user_name + '_' + img_name;
			fs.readFile(filepath, function(err, data){
				res.writeHead(200, {"Context-Type":"image/jpg"});
				res.write(data);
				res.end();
			});
			console.log('- Analyzed pore image response done.\n');
		}
	});
});

// 주름 이미지 분석 요청
app.post('/:user_name/wrinkle', up_img.single('img'), function(req, res){
	var user_name = req.params.user_name;
	var img_name = req.file.originalname;
	console.log('# Wrinkle image analysis requrest arrive. (' + user_name + ' : ' + img_name + ')');
	var options = {
		mode : 'text',
		pythonPath:'/usr/bin/python3',
		pythonOptions:['-u'],
		args: [user_name, img_name]
	};
	PythonShell.run('wrinkle.py', options, function(err){
		if(err) throw err;
		else {
			console.log('- Wrinkle image analysis done.');
			var filepath = 'UserData/wrinkle_' + user_name + '_' + img_name;
			fs.readFile(filepath, function(err, data){
				res.writeHead(200, {"Context-Type":"image/jpg"});
				res.write(data);
				res.end();
			});
			console.log('- Analyzed wrinkle image response done.\n');
		}
	});
});

// 모공 이미지 개수 구하기
app.get('/select/:user_name/poreindex', function(req,res){
	var user_name = req.params.user_name;
	var count = 0;
	fs.readdir('./UserData', function(error, filelist){
		for( var i = 0; i < filelist.length; i++ ) {
			var file_name = filelist[i];
			if(file_name.indexOf(user_name) !== -1){
				if(file_name.indexOf('pore') !== -1)
				{
					count = count + 1;
				}
			}
		}

		console.log('\n# Analyzed pore image index. (' + user_name + ' : ' + count + ')\n');
		res.writeHead(200, {"Context-Type":"text/plain"});
		res.end(String(count));
	});
});

// 모공 이미지 다운로드
app.get('/select/:user_name/pore/:index', function(req, res){
	var user_name = req.params.user_name;
	var index = req.params.index;
	index = Number(index);
	var count = 0;
	fs.readdir('./UserData', function(error, filelist){
		for( var i = 0; i < filelist.length; i++ ) {
			var file_name = filelist[i];
			if(file_name.indexOf(user_name) !== -1){
				if(file_name.indexOf('pore') !== -1)
				{
					if(index === count) break;
					count = count + 1;
				}
			}
		}
		var file_path = './UserData/' + file_name;
		console.log('# Analyzed pore image download. (' + user_name + ' : ' + file_name + '\n');
		fs.readFile(file_path, function(err, data){
			res.writeHead(200, {"Context-Type":"image/jpg"});
			res.write(data);
			res.end();
		});
	});
});

// 모공 이미지 이름 구하기
app.get('/select/:user_name/porename/:index', function(req,res){
	var user_name = req.params.user_name;
	var index = req.params.index;
	index = Number(index);
	var count = 0;
	
	fs.readdir('./UserData', function(error, filelist){
		for( var i = 0; i < filelist.length; i++ ) {
			var file_name = filelist[i];
			if(file_name.indexOf(user_name) !== -1){
				if(file_name.indexOf('pore') !== -1)
				{
					if(index === count) break;
					count = count + 1;
				}
			}
		}
		console.log('# Analyzed pore image name get. (' + user_name + ' : ' + file_name + '\n');
		res.writeHead(200, {"Context-Type":"text/plain"});
		res.end(file_name);
	});
});

// 주름 이미지 개수 구하기
app.get('/select/:user_name/Wrinkleindex', function(req,res){
	var user_name = req.params.user_name;
	var count = 0;
	fs.readdir('./UserData', function(error, filelist){
		for( var i = 0; i < filelist.length; i++ ) {
			var file_name = filelist[i];
			if(file_name.indexOf(user_name) !== -1){
				if(file_name.indexOf('wrinkle') !== -1)
				{
					count = count + 1;
				}
			}
		}
		console.log('# Analyzed wrinkle image index. (' + user_name + ' : ' + count + ')\n');
		res.writeHead(200, {"Context-Type":"text/plain"});
		res.end(String(count));
	});
});

// 주름 이미지 다운로드
app.get('/select/:user_name/Wrinkle/:index', function(req, res){
	var user_name = req.params.user_name;
	var index = req.params.index;
	index = Number(index);
	var count = 0;

	fs.readdir('./UserData', function(error, filelist){
		for( var i = 0; i < filelist.length; i++ ) {
			var file_name = filelist[i];
			if(file_name.indexOf(user_name) !== -1){
				if(file_name.indexOf('wrinkle') !== -1)
				{
					if(index === count) break;
					count = count + 1;
				}
			}
		}
		var file_path = './UserData/' + file_name;
		console.log('# Analyzed wrinkle image download. (' + user_name + ' : ' + file_name + '\n');
		fs.readFile(file_path, function(err, data){
			res.writeHead(200, {"Context-Type":"image/jpg"});
			res.write(data);
			res.end();
		});
	});
});


// 주름 이미지 이름 가져오기
app.get('/select/:user_name/Wrinklename/:index', function(req,res){
	var user_name = req.params.user_name;
	var index = req.params.index;
	index = Number(index);
	var count = 0;
	
	fs.readdir('./UserData', function(error, filelist){
		for( var i = 0; i < filelist.length; i++ ) {
			var file_name = filelist[i];
			if(file_name.indexOf(user_name) !== -1){
				if(file_name.indexOf('wrinkle') !== -1)
				{
					if(index === count) break;
					count = count + 1;
				}
			}
		}
		console.log('# Analyzed pore image name get. (' + user_name + ' : ' + file_name + '\n');
		res.writeHead(200, {"Context-Type":"text/plain"});
		res.end(file_name);
	});
});

// 유저 회원가입
app.post('/user/join', function (req, res) {
	console.log('# User join reuqest arrive.');
    console.log(req.body);
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var userName = req.body.userName;
    var userPhone = req.body.userPhone;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO Users (UserEmail, UserPwd, UserName, UserPhone) VALUES (?, ?, ?, ?)';
    var params = [userEmail, userPwd, userName, userPhone];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
			message = '회원가입에 성공했습니다.';
			console.log('- Join success.\n');
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

//유저 로그인
app.post('/user/login', function (req, res) {
    var userEmail = req.body.userEmail;
	var userPwd = req.body.userPwd;
	console.log('# User login reuqest arrive. (' + userEmail + ')');
    var sql = 'select * from Users where UserEmail = ?';

    connection.query(sql, userEmail, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
	var info = [''];

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
				message = '존재하지 않는 계정입니다!';
				console.log('- No matched ID.\n');
            } else if (userPwd !== result[0].UserPwd) {
                resultCode = 204;
				message = '비밀번호가 틀렸습니다!';
				console.log('- No matched Pwd.\n');
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].UserName + '님 환영합니다!';
				info = [result[0].UserEmail, result[0].UserPwd, result[0].UserName, result[0].UserPhone];
				console.log('- Login success.\n');
            }
        }


        res.json({
            'code': resultCode,
            'message': message,
	    'info': info
        });
    })
});

app.post('/user/modify', function(req, res){
	var userEmail = req.body.userEmail;
	console.log('# User information modify reuqest arrive. (' + userEmail + ')');
	var userPwd = req.body.userPwd;
	var userPhone = req.body.userPhone;
	var sql = 'update Users set UserPwd = ?, UserPhone = ? where UserEmail = ?';

	connection.query(sql, [userPwd, userPhone, userEmail], function(err, result){
		var resultCode = 404;
		var message = '에러가 발생했습니다';
	
		if(err){
			console.log(err);
		}else{
			resultCode = 200;
			message = '회원 정보 수정 성공!';
			console.log('- Modify success.\n');
		}

		res.json({
			'code': resultCode,
			'message': message
		});
	})
});


