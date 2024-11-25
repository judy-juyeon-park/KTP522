var mongoose = require('mongoose')
var Schema = mongoose.Schema

// AS7. 동적 스키마 및 모듈화 된 테이블 설계 
var taleSchema = new Schema({
    title : String, 
    version : { type: Number, default: 1.0 },
    context : [String],
    image : [String]
},{ timestamps: true, versionKey:'_somethingElse'})

module.exports = mongoose.model('tale',taleSchema)