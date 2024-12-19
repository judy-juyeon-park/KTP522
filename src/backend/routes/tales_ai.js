// var express = require('express');
// var router = express.Router();
// var AImodel = require('../module/ai_model');
// var talelog = require('../model/talelog');

// /**
//  * @openapi
//  * '/api/talesAI/story':
//  *  get:
//  *     tags:
//  *     - TaleAI APIs 
//  *     summary: 사용자 정보 기반 동화 생성
//  *     requestBody:
//  *      required: true
//  *      content:
//  *          application/json:
//  *              schema:
//  *                  required:
//  *                  - name
//  *                  - gender
//  *                  properties:
//  *                      name:
//  *                          type: string
//  *                      gender:
//  *                          type: string
//  *     responses:
//  *      200:
//  *        description: Success
//  *        content:
//  *          application/json:
//  *            schema:
//  *              type: object
//  *              properties:
//  *                code:
//  *                  type: integer
//  *                message:
//  *                  type: string
//  *                data:
//  *                  type: object
//  *                  properties:
//  *                      title: 
//  *                          type: string
//  *                      story: 
//  *                          type: array
//  *                          items:
//  *                              type: string
//  *      400:
//  *        description: Invalid Input
//  *      422:
//  *          description: AI Error
//  *      500:
//  *        description: Server Error
//  */

// // 동화 생성
// router.get('/story',async(req, res, next)=>{

//     try{
//         const { name, gender }  = req.body;

//         //파라미터체크
//         if(!name || !gender ){
//             res.json({
//                 code : 400,
//                 message: "입력 정보가 부족합니다."
//             });
//         return;
//         }   

//         try{
//         const story = await AImodel.generateStory(name, gender);
//         res.json({
//             code: 200,
//             message:"동화를 생성했습니다.",
//             data: {
//                 title : story.at(0).replace(/^\*+|\*+$/g, ""),
//                 story : story.slice(1)
//             }
//         })
//         return;

//         } catch(err){
//             if(err){
//                 console.log(err);
//                 res.json({
//                     code: 422,
//                     message: "동화생성 오류가 발생했습니다."
//                 })
//                 return;
//             }
//         }

//     }catch (err) { 
//         if(err){
//             console.log(err);
//             res.json({
//                 code: 500,
//                 message: "서버오류가 발생했습니다."
//             })
//             return;
//         }
//     }
// });

// /**
//  * @openapi
//  * '/api/talesAI/illust':
//  *  get:
//  *     tags:
//  *     - TaleAI APIs 
//  *     summary: 사용자 정보 기반 삽화 생성
//  *     requestBody:
//  *      required: true
//  *      content:
//  *          application/json:
//  *              schema:
//  *                  required:
//  *                  - page
//  *                  - paragraph
//  *                  - gender
//  *                  properties:
//  *                      page:
//  *                          type: integer
//  *                      paragraph:
//  *                          type: string
//  *                      gender:
//  *                          type: string
//  *     responses:
//  *      200:
//  *        description: Success
//  *        content:
//  *          application/json:
//  *            schema:
//  *              type: object
//  *              properties:
//   *                code:
//  *                  type: integer
//  *                message:
//  *                  type: string
//  *                data:
//  *                  type: object
//  *                  properties:
//  *                      page:
//  *                          type: integer
//  *                      image:
//  *                          type: string
//  *      400:
//  *        description: Invalid Input
//  *      422:
//  *          description: AI Error
//  *      500:
//  *        description: Server Error
//  */

// // 삽화 생성
// router.get('/illust',async(req, res, next)=>{

//     try{
//         const { page, paragraph, gender }  = req.body;

//         //파라미터체크
//         if(!paragraph || !gender ){
//             res.json({
//                 code : 400,
//                 message: "입력 정보가 부족합니다."
//             });
//         return;
//         }   

//         try{
//         const imagePath = await AImodel.generateIllustration(paragraph, gender);
//         res.json({
//             code: 200,
//             message:"삽화를 생성했습니다.",
//             data: {
//                 page : page,
//                 image : imagePath
//             }
//         })
//         return;
//         }catch(err) {
//             if(err){
//                 console.log(err);
//                 res.json({
//                     code: 422,
//                     message: "삽화생성 오류가 발생했습니다."
//                 })
//                 return;
//             }
//         }

//     }catch (err) { 
//         if(err){
//             console.log(err);
//             res.json({
//                 code: 500,
//                 message: "서버오류가 발생했습니다."
//             })
//             return;
//         }
//     }
// });

// /**
//  * @openapi
//  * '/api/talesAI/like':
//  *  get:
//  *     tags:
//  *     - TaleAI APIs 
//  *     summary: 생성 동화 로깅
//  *     requestBody:
//  *      required: true
//  *      content:
//  *          application/json:
//  *              schema:
//  *                  required:
//  *                  - title
//  *                  - story
//  *                  properties:
//  *                      title:
//  *                          type: string
//  *                      story:
//  *                          type: array
//  *                          items:
//  *                              type : string
//  *     responses:
//  *      200:
//  *        description: Success
//  *        content:
//  *          application/json:
//  *            schema:
//  *              type: object
//  *              properties:
//   *                code:
//  *                  type: integer
//  *                message:
//  *                  type: string
//  *      400:
//  *        description: Invalid Input
//  *      500:
//  *        description: Server Error
//  */
// // 생성 동화 로깅
// router.post('/like', async(req, res, next)=>{
//     try{
//         const { title, story }  = req.body;

//         //파라미터체크
//         if(!title || !story ){
//             res.json({
//                 code : 400,
//                 message: "입력 정보가 부족합니다."
//             });
//         return;
//         }   

//         //생성동화 로깅
//         var newTalelog = new talelog();
//         newTalelog.title = title;
//         newTalelog.story = story;
//         newTalelog.save()
//         .then((newTalelog) =>{
//             res.json({
//                 code: 200,
//                 message:"생성된 동화를 로깅했습니다."
//                 // data: {
//                 //     title: newTalelog.title,
//                 //     story: newTalelog.story,
//                 //     createdAt: newTalelog.createdAt
//                 // }
//             })
//             return;
//         })

//     }catch (err) { 
//         if(err){
//             console.log(err);
//             res.json({
//                 code: 500,
//                 message: "서버오류가 발생했습니다."
//             })
//             return;
//         }
//     }
// });


// module.exports = router;
var express = require('express');
var router = express.Router();
var AImodel = require('../module/ai_model');
var talelog = require('../model/talelog');

/**
 * @openapi
 * '/api/talesAI/story':
 *  post:
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
router.post('/story', async (req, res, next) => {
    try {
        const { name, gender } = req.body;

        // 파라미터체크
        if (!name || !gender) {
            res.json({
                code: 400,
                message: "입력 정보가 부족합니다."
            });
            return;
        }

        try {
            //const story = await AImodel.generateStory(name, gender);
            res.json({
                code: 200,
                message: "동화를 생성했습니다.",
                data: {
                    title: "니키와 마법의 나비"//story.at(0).replace(/^\*+|\*+$/g, ""),
                    ,story: [
            "옛날 옛적, 작은 마을의 끝자락에 사는 소녀 니키가 있었습니다. 니키는 언제나 호기심이 가득하고 상상력이 풍부한 아이였습니다. 매일 같은 길을 걸어가는 것조차도 그녀에게는 새로운 모험이었죠. 특히, 그녀는 마을 숲 속에 사는 동물들과 이야기를 나누는 것을 좋아했습니다. 니키는 동물 친구들에게 자신이 상상한 이야기를 들려주곤 했습니다.",
            "어느 날, 니키는 숲 속에서 반짝이는 무언가를 발견했습니다. 그건 바로 화려한 날개를 가진 마법의 나비였습니다. 나비는 니키를 보고 반짝이는 눈으로 그녀에게 다가왔습니다. “안녕, 니키! 나는 꿈을 실현시켜주는 마법의 나비야!” 나비는 자신을 소개하며, 니키에게 특별한 소원을 들어줄 수 있다고 말했습니다. 니키는 그 순간 가슴이 두근거리며 마법의 나비에게 소원을 빌기로 결심했습니다.",
            "“나는 친구들과 함께 신나는 모험을 하고 싶어요!” 니키는 소리쳤습니다. 그러자 나비는 날개를 퍼덕이며 빛나는 먼지를 뿌렸습니다. 순간, 니키와 그녀의 친구들은 마법의 숲 속으로 들어가게 되었고, 그곳은 상상 속에서만 볼 수 있는 신비로운 장소였습니다. 나무들은 노래를 부르고 꽃들은 춤을 추었으며, 하늘에서는 형형색색의 무지개가 떠 있었습니다.",
            "모험 중에 니키와 친구들은 다양한 동물 친구들을 만났습니다. 그들은 함께 숨바꼭질도 하고, 나무 위에서 미끄럼틀 타기를 하며 즐거운 시간을 보냈습니다. 특히, 니키는 나비와 함께 하늘을 나는 기분을 만끽했습니다. 나비는 니키에게 다양한 비밀을 가르쳐주며 그녀의 상상력이 얼마나 중요한지를 깨닫게 해주었습니다.",
            "그러나 시간이 흐르면서 니키는 친구들과 함께하는 시간이 점점 줄어드는 것을 느꼈습니다. 나비는 그녀에게 돌아갈 시간을"
        ]//story.slice(1)
                }
            });
            return;
        } catch (err) {
            console.log(err);
            res.json({
                code: 422,
                message: "동화생성 오류가 발생했습니다."
            });
            return;
        }
    } catch (err) {
        console.log(err);
        res.json({
            code: 500,
            message: "서버오류가 발생했습니다."
        });
        return;
    }
});

/**
 * @openapi
 * '/api/talesAI/illust':
 *  post:
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
router.post('/illust', async (req, res, next) => {
    try {
        const { page, paragraph, gender } = req.body;

        // 파라미터체크
        if (!paragraph || !gender) {
            res.json({
                code: 400,
                message: "입력 정보가 부족합니다."
            });
            return;
        }

        try {
           // const imagePath = await AImodel.generateIllustration(paragraph, gender);
            res.json({
                code: 200,
                message: "삽화를 생성했습니다.",
                data: {
                    page: page,
                    image: "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Ts5LY8tZ4cpdj1ANhtUOlAyc/user-yQREIjZkdeFogbEPCieoZAQj/img-nh7yAob717mcRkIQDtuxjzyg.png?st=2024-12-13T06%3A51%3A39Z&se=2024-12-13T08%3A51%3A39Z&sp=r&sv=2024-08-04&sr=b&rscd=inline&rsct=image/png&skoid=d505667d-d6c1-4a0a-bac7-5c84a87759f8&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2024-12-13T03%3A08%3A17Z&ske=2024-12-14T03%3A08%3A17Z&sks=b&skv=2024-08-04&sig=k7FPDxV2t5tmbRhtvu7DVOcqcHqR6yYCXZYsRji6RVE%3D"//imagePath
                }
            });
            return;
        } catch (err) {
            console.log(err);
            res.json({
                code: 422,
                message: "삽화생성 오류가 발생했습니다."
            });
            return;
        }
    } catch (err) {
        console.log(err);
        res.json({
            code: 500,
            message: "서버오류가 발생했습니다."
        });
        return;
    }
});

/**
 * @openapi
 * '/api/talesAI/like':
 *  post:
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
 *      400:
 *        description: Invalid Input
 *      500:
 *        description: Server Error
 */

// 생성 동화 로깅
router.post('/like', async (req, res, next) => {
    try {
        const { title, story } = req.body;

        // 파라미터체크
        if (!title || !story) {
            res.json({
                code: 400,
                message: "입력 정보가 부족합니다."
            });
            return;
        }

        // 생성동화 로깅
        var newTalelog = new talelog();
        newTalelog.title = title;
        newTalelog.story = story;
        newTalelog.save()
            .then(() => {
                res.json({
                    code: 200,
                    message: "생성된 동화를 로깅했습니다."
                });
                return;
            });
    } catch (err) {
        console.log(err);
        res.json({
            code: 500,
            message: "서버오류가 발생했습니다."
        });
        return;
    }
});

module.exports = router;
