var express = require('express');
var router = express.Router();
var AImodel = require('../module/ai_model');
var talelog = require('../model/talelog');

/**
 * @openapi
 * '/api/talesAI/story':
 *  get:
 *     tags:
 *     - TaleAI APIs 
 *     summary: 사용자 정보 기반 동화 생성
 *     requestBody:
 *      required: true
 *      content:
 *          application/json:
 *              schema:
 *                  required:
 *                  - name
 *                  - gender
 *                  properties:
 *                      name:
 *                          type: string
 *                      gender:
 *                          type: string
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
 *                      story: 
 *                          type: array
 *                          items:
 *                              type: string
 *      400:
 *        description: Invalid Input
 *      422:
 *          description: AI Error
 *      500:
 *        description: Server Error
 */

// 동화 생성
router.get('/story',async(req, res, next)=>{

    try{
        const { name, gender }  = req.body;

        //파라미터체크
        if(!name || !gender ){
            res.json({
                code : 400,
                message: "입력 정보가 부족합니다."
            });
        return;
        }   

        try{
        const story = await AImodel.generateStory(name, gender);
        res.json({
            code: 200,
            message:"동화를 생성했습니다.",
            data: {
                title : story.at(0).replace(/^\*+|\*+$/g, ""),
                story : story.slice(1)
            }
        })
        return;

        } catch(err){
            if(err){
                console.log(err);
                res.json({
                    code: 422,
                    message: "동화생성 오류가 발생했습니다."
                })
                return;
            }
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

/**
 * @openapi
 * '/api/talesAI/illust':
 *  get:
 *     tags:
 *     - TaleAI APIs 
 *     summary: 사용자 정보 기반 삽화 생성
 *     requestBody:
 *      required: true
 *      content:
 *          application/json:
 *              schema:
 *                  required:
 *                  - page
 *                  - paragraph
 *                  - gender
 *                  properties:
 *                      page:
 *                          type: integer
 *                      paragraph:
 *                          type: string
 *                      gender:
 *                          type: string
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
 *                      page:
 *                          type: integer
 *                      image:
 *                          type: string
 *      400:
 *        description: Invalid Input
 *      422:
 *          description: AI Error
 *      500:
 *        description: Server Error
 */

// 삽화 생성
router.get('/illust',async(req, res, next)=>{

    try{
        const { page, paragraph, gender }  = req.body;

        //파라미터체크
        if(!paragraph || !gender ){
            res.json({
                code : 400,
                message: "입력 정보가 부족합니다."
            });
        return;
        }   

        try{
        const imagePath = await AImodel.generateIllustration(paragraph, gender);
        res.json({
            code: 200,
            message:"삽화를 생성했습니다.",
            data: {
                page : page,
                image : imagePath
            }
        })
        return;
        }catch(err) {
            if(err){
                console.log(err);
                res.json({
                    code: 422,
                    message: "삽화생성 오류가 발생했습니다."
                })
                return;
            }
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

/**
 * @openapi
 * '/api/talesAI/like':
 *  get:
 *     tags:
 *     - TaleAI APIs 
 *     summary: 생성 동화 로깅
 *     requestBody:
 *      required: true
 *      content:
 *          application/json:
 *              schema:
 *                  required:
 *                  - title
 *                  - story
 *                  properties:
 *                      title:
 *                          type: string
 *                      story:
 *                          type: array
 *                          items:
 *                              type : string
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
 *                      story:
 *                          type: array
 *                          items:
 *                              type : string
 *                      createdAt:
 *                          type: string
 *                          format: date-time
 *      400:
 *        description: Invalid Input
 *      500:
 *        description: Server Error
 */
// 생성 동화 로깅
router.post('/like', async(req, res, next)=>{
    try{
        const { title, story }  = req.body;

        //파라미터체크
        if(!title || !story ){
            res.json({
                code : 400,
                message: "입력 정보가 부족합니다."
            });
        return;
        }   

        //생성동화 로깅
        var newTalelog = new talelog();
        newTalelog.title = title;
        newTalelog.story = story;
        newTalelog.save()
        .then((newTalelog) =>{
            res.json({
                code: 200,
                message:"생성된 동화를 로깅했습니다.",
                data: {
                    title: newTalelog.title,
                    story: newTalelog.story,
                    createdAt: newTalelog.createdAt
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


module.exports = router;