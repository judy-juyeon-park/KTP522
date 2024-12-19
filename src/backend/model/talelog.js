var mongoose = require('mongoose')
var Schema = mongoose.Schema

// AS7. 동적 스키마 및 모듈화 된 테이블 설계 
var talelogSchema = new Schema({
    title : String, 
    story : [String]
},{ timestamps: true, versionKey:'_somethingElse'})

module.exports = mongoose.model('talelog',talelogSchema)