var mongoose = require('mongoose')
var Schema = mongoose.Schema

var taleSchema = new Schema({
    title : String, 
    version : { type: Number, default: 1.0 },
    context : [String],
    image : [String]
},{ timestamps: true, versionKey:'_somethingElse'})

module.exports = mongoose.model('tale',taleSchema)