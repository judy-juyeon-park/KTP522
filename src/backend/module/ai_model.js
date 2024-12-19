// openAI API
const path = require("path");
const axios = require("axios");
//const fs = require("fs");
// OpenAI API 키 설정
const openAI = require("../config/openAI.json")
const OPENAI_API_KEY = openAI.apikey;

const storySample  = [
    "주연과 신비한 동화의 나라",
    "주연은 작은 마을에 사는 밝고 사랑스러운 소녀였습니다. 그녀는 매일 저녁 별이 반짝이는 하늘을 바라보며 꿈꾸기를 좋아했습니다. \"나는 언젠가 꿈의 나라로 여행을 갈 거야!\"라고 말하곤 했습니다. 그녀의 눈빛은 언제나 호기심으로 가득 차 있었고, 마을 사람들은 그녀의 꿈을 응원했습니다.",
    "어느 날, 주연은 꿈의 나라로 가는 특별한 꿈을 꿨습니다. 금색 구름이 그녀를 감싸더니, 신비한 정원으로 데려다 주었습니다. 그곳은 환상적인 꽃들이 만발해 있었고, 나비들은 노래를 부르며 춤을 추고 있었습니다. 주연은 깜짝 놀라며 정원 한가운데로 걸어갔습니다. 그곳에서 만난 작은 요정은 주연에게 \"환영해! 네가 이곳의 특별한 손님이야!\"라고 말했습니다.",
    "주연은 요정과 함께 정원 탐험을 시작했습니다. 요정은 다양한 꽃들의 이름을 알려주었고, 주연은 그 꽃들을 따서 아름다운 꽃다발을 만들었습니다. \"이 꽃들은 너의 꿈을 더욱 빛나게 해줄 거야!\"라고 요정이 말했습니다. 주연은 점점 더 이곳이 자기를 위해 만들어진 것처럼 느껴졌습니다.",
    "그러나 갑자기 하늘이 어두워지며 구름이 몰려왔습니다. 꿈의 나라에 이상한 일이 생기고 있었습니다. 요정은 주연에게 \"우리가 이곳을 지켜야 해! 함께 힘을 합쳐야 해!\"라고 외쳤습니다. 주연은 용기를 내어 요정과 함께 마법의 꽃들을 이용해 구름을 쫓아냈습니다. 그들은 환상의 힘으로 꿈의 나라를 다시 밝히고, 행복한 분위기를 되살렸습니다.",
    "해가 다시 떠오르자 주연과 요정은 서로의 손을 잡고 웃으며 기뻐했습니다. \"너는 정말 용감한 소녀야! 너의 꿈은 언제나 이곳에 남아 있을 거야!\"라고 요정이 말했습니다"
];
const imageURLhSample = "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Ts5LY8tZ4cpdj1ANhtUOlAyc/user-yQREIjZkdeFogbEPCieoZAQj/img-ygLFjPHGbtwe9Yi4dE1u4HGQ.png?st=2024-12-14T07%3A27%3A26Z&se=2024-12-14T09%3A27%3A26Z&sp=r&sv=2024-08-04&sr=b&rscd=inline&rsct=image/png&skoid=d505667d-d6c1-4a0a-bac7-5c84a87759f8&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2024-12-13T17%3A28%3A59Z&ske=2024-12-14T17%3A28%3A59Z&sks=b&skv=2024-08-04&sig=fwoabphV3LvGym3WM2703LgnitxG86ti87aiMLXk37k%3D";

module.exports = {

    // 동화 생성
    generateStory : async (userName, gender) => {
    
    
    // 고정동화 리턴처리 
    return storySample; // <-----이부분 주석처리하면 됨

    // System Prompt 설정
    const systemPrompt = "너는 창의적이고 상상력이 풍부한 동화 작가입니다. 어린이를 위한 재미있고 따뜻한 이야기를 800자 이내의 한국어로 작성하세요.";

    // User Prompt 설정
    const userPrompt = `
        다음 조건에 맞는 6문단의 동화를 작성하세요:
        1. 주인공 이름은 ${userName}입니다.
        2. 주인공은 ${gender}
        3. 이야기는 어린이에게 적합해야 하고 상상력이 가득해야 합니다.
        4. 마지막에는 행복한 결말로 완결되게 끝나야 합니다.
        5. 첫문단은 제목 문단입니다. 
        6. 두번째 문단부터 여섯번째 문단까지는 내용입니다. 
    `;

    try {
        const response = await axios.post(
            "https://api.openai.com/v1/chat/completions",
            {
                model: "gpt-4o-mini", // 모델 설정
                messages: [
                    { role: "system", content: systemPrompt },
                    { role: "user", content: userPrompt }
                ],
                max_tokens: 500,
                temperature: 0.8,
            },
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${OPENAI_API_KEY}`,
                },
                //timeout: 5000
            }
        );

        // 생성된 텍스트 추출
        const story = response.data.choices[0].message.content.trim().replace(/^###\s*/, "");

        // 제목포함 6문단으로 나누기
        const paragraphs = story.split("\n").filter((p) => p.trim() !== "");

        // 반환값: 제목포함 5문단 배열
        return paragraphs.length >= 6 ? paragraphs.slice(0, 6) : paragraphs;
    } catch (error) {
        console.error("Error generating story:", error.response ? error.response.data : error.message);
        return [];
    }
},

    // 삽화 생성
    generateIllustration : async (paragraph, gender) => {
    const prompt = `
        Create a detailed and imaginative illustration for the following paragraph from a children's story:
        "${paragraph}"
        The illustration should be vibrant, colorful, and suitable for a children's book.
        Main character is ${gender}.
    `;

    // 고정URL 리턴처리
    return imageURLhSample; // <-----이부분 주석처리하면 됨

    try {
        const response = await axios.post(
            "https://api.openai.com/v1/images/generations",
            {
                prompt: prompt,
                n: 1, // 한 개의 이미지 생성
                size: "1024x1024", // 이미지 크기
                model: "dall-e-3", // 삽화 생성모델 
            },
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${OPENAI_API_KEY}`,
                },
            }
        );

        // 이미지 URL 추출
        const imageUrl = response.data.data[0].url;
        return imageUrl;

        // // 이미지 다운로드 및 저장
        // const imageResponse = await axios.get(imageUrl, { responseType: "arraybuffer" });
        // const outputPath = path.join(__dirname, "illustration.png");
        // fs.writeFileSync(outputPath, imageResponse.data);

        // console.log("Image saved to:", outputPath);
        // return outputPath;
    } catch (error) {
        console.error("Error generating illustration:", error.response ? error.response.data : error.message);
        return null;
    }
}

// 동화 생성 예시 실행
// (async () => {
//     const userName = "니키"; // 사용자 이름
//     const gender = "소녀";
//     const story = await generateStory(userName, gender);
//     console.log("Generated Story in Korean:", story);
// })();

// 삽화 예시 실행
// (async () => {
//     const paragraph = "니키는 하늘을 날며 숲 속 친구들에게 도움을 주기 시작했습니다. 길을 잃은 토끼를 집으로 데려다주고, 슬퍼하는 새를 위로하며 그녀는 모두의 영웅이 되었어요.";
//     const illustrationPath = await generateIllustration(paragraph);

//     if (illustrationPath) {
//         console.log("Generated illustration saved at:", illustrationPath);
//     } else {
//         console.log("Failed to generate illustration.");
//     }
// })();

}