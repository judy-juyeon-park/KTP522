// openAI API
const path = require("path");
const axios = require("axios");
//const fs = require("fs");
// OpenAI API 키 설정
const openAI = require("../config/openAI.json")
const OPENAI_API_KEY = openAI.apikey;

module.exports = {

    // 동화 생성
    generateStory : async (userName, gender) => {
    // System Prompt 설정
    const systemPrompt = "너는 창의적이고 상상력이 풍부한 동화 작가입니다. 어린이를 위한 재미있고 따뜻한 이야기를 한국어로 작성하세요.";

    // User Prompt 설정
    const userPrompt = `
        다음 조건에 맞는 6문단의 동화를 작성하세요:
        1. 주인공 이름은 ${userName}입니다.
        2. 주인공은 ${gender}
        3. 이야기는 어린이에게 적합해야 하고 상상력이 가득해야 합니다.
        4. 마지막에는 행복한 결말로 끝나야 합니다.
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

	console.log("Response Data : \n" + response.data);
        // 생성된 텍스트 추출
        const story = response.data.choices[0].message.content.trim();
	
	console.log("Story : \n"+story);
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

    try {
        const response = await axios.post(
            "https://api.openai.com/v1/images/generations",
            {
                prompt: prompt,
                n: 1, // 한 개의 이미지 생성
                size: "1024x1024", // 이미지 크기
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
