// AS6. 표준화된 API 응답 구조 설계 (JSON, XML 등) 
const swaggerUi = require("swagger-ui-express")
const swaggereJsdoc = require("swagger-jsdoc")

const options = {
  swaggerDefinition: {
    openapi: "3.0.0",
    info: {
      version: "1.0.0",
      title: "Tales Voice",
      description:
        "동화읽어주기 시스템 API 문서",
    },
    servers: [
        {
            url: "localhost:3000",
            description: "Local Development",
          },
          {
            url: "40.82.138.108",
            description: "Azure Server",
          },
    ],
  },
  apis: ["./routes/*.js", "./routes/user/*.js"], //Swagger 파일 연동
}
const specs = swaggereJsdoc(options)

module.exports = { swaggerUi, specs }