var express = require('express');
var router = express.Router();
var tale = require('../model/tale');

/* Create tale */
router.post('/', async(req, res, next)=>{
    try{
        const { title, version, context, image }  = req.body;

        //파라미터체크
        if(!title || !context || !image){
            res.json({
                code : 400,
                message: "입력 정보가 부족합니다."
            });
        return;
        }   

        //중복체크
        const result = await tale.findOne({title:title})
        if(result){
            res.json({
                code : 400,
                message:"동일한 이름의 동화가 존재합니다."
            })
            return;
        }

        //동화데이터 생성
        var newTale = new tale();
        newTale.title = title;
        newTale.version = version;
        newTale.context = context;
        newTale.image = image;
        newTale.save()
        .then((newTale) =>{
            res.json({
                code: 200,
                message:"동화를 추가했습니다.",
                data: {
                    id: newTale._id,
                    title: newTale.title,
                    version: newTale.version,
                    createdAt: newTale.createdAt
                }
            })
            return;
        })

    }catch (err) { 
        if(err){
            console.log(err);
            res.json({
                code: 500,
                message: "서버오류가 발생했습니다."
            })
            return;
        }
    }
});

/* Update tale */
router.put('/:id', async(req, res, next)=>{
    try{
        const reqID = req.params.id;
        const { title, version, context, image }  = req.body;
        
        //id체크
        var result = await tale.findOne({_id:reqID});
        if(!reqID || !result){
            console.log(reqID, result);
            res.json({
                code : 400,
                message: "올바른 동화 id를 입력하세요."
            });
        return;
        }   

        //파라미터체크
        if(!title || !context || !image){
            res.json({
                code : 400,
                message: "업데이트 정보가 부족합니다."
            });
        return;
        }   

        //동화데이터 생성
        result.title = title;
        result.version = version;
        result.context = context;
        result.image = image;
        result.save()
        .then((result) =>{
            res.json({
                code: 200,
                message:"동화를 업데이트 했습니다.",
                data: {
                    id: result._id,
                    title: result.title,
                    version: result.version,
                    context: result.context,
                    image: result.image,
                    createdAt: result.createdAt,
                    updatedAt: result.updatedAt
                }
            })
            return;
        })

    }catch (err) { 
        if(err){
            console.log(err);
            res.json({
                code: 500,
                message: "서버오류가 발생했습니다."
            })
            return;
        }
    }
});

/* DELETE tale */
router.delete('/:id', function(req, res, next){
    try{
        const reqID = req.params.id;
        tale.deleteOne({_id:reqID})
        .then(()=>{
            res.json({
                code: 200,
                message:"동화를 삭제했습니다.",
            })
            return;
        })
        
    }catch (err) { 
        if(err){
            console.log(err);
            res.json({
                code: 500,
                message: "서버오류가 발생했습니다."
            })
            return;
        }
    }
});

/**
 * @openapi
 * '/api/tales/list':
 *  get:
 *     tags:
 *     - Tale APIs
 *     summary: 전체 동화 리스트 조회
 *     responses:
 *      200:
 *        description: Success
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                code:
 *                  type: integer
 *                message:
 *                  type: string
 *                data:
 *                  type: array
 *                  items:
 *                      type: object
 *                      properties:
 *                          id: 
 *                              type: integer
 *                          title:
 *                              type: string
 *                          version:
 *                              type: string
 *                total:
 *                  type: integer
 *      400:
 *        description: List Empty
 *      500:
 *        description: Server Error
 */

/* GET tales list */
router.get('/list', async(req, res, next)=>{
    try{
        const talesList = await tale.find();
        if(!talesList){
            res.json({
                code : 400,
                message:"동화가 존재하지 않습니다."
            })
            return;
        }

        var data = await talesList.map((item, index) => ({
            id: item._id,
            title: item.title,
            version: item.version,
            //context: item.context,
            //image: item.image,
            //createdAt: item.createdAt,
            //updatedAt: item.updatedAt
        }));
        
        res.json({
            code: 200,
            message: "동화 리스트를 조회했습니다.",
            data: data,
            totla: data.length
        })
        return;

    }catch (err) { 
        if(err){
            console.log(err);
            res.json({
                code : 500,
                message:"서버오류가 발생했습니다."
            })
            return;
        }
    }
});


/**
 * @openapi
 * '/api/tales/content/{id}':
 *  get:
 *     tags:
 *     - Tale APIs
 *     summary: 동화 내용 조회
 *     parameters:
 *      - name: id
 *        in: query
 *        description: tale id
 *        required: true
 *        schema:
 *          type: string
 *     responses:
 *      200:
 *        description: Success
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                code:
 *                  type: integer
 *                message:
 *                  type: string
 *                data:
 *                  type: object
 *                  properties:
 *                      title:
 *                          type: string
 *                      version:
 *                          type: string
 *                      context:
 *                          type: array
 *                          items:
 *                              type: string
 *                      image:
 *                          type: array
 *                          items:
 *                              type: string
 *      400:
 *        description: Invalid ID
 *      500:
 *        description: Server Error
 */

/* GET tales content */
router.get('/content/:id', async (req, res, next)=>{
    try{
        const reqID = req.params.id;
        if(!reqID ){ 
            res.json({
                code : 400,
                message: "동화 id를 입력하세요."
            });
        return;
        }   

        const result = await tale.findOne({_id:reqID})
        if(!result){
            res.json({
                code : 400,
                message: "올바른 동화 id를 입력하세요."
            })
            return;
        }else{
            res.json({
                code: 200,
                message: "동화 내용을 조회했습니다.",
                data: {
                    title: result.title,
                    version: result.version,
                    context: result.context,
                    image: result.image
                }
            })
            return;
        }

    }catch (err) { 
        if(err){
            console.log(err);
            res.json({
                code: 500,
                message: "서버오류가 발생했습니다."
            })
            return;
        }
    }
    
});



module.exports = router;
