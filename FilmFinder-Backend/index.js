const fs = require('fs');
const csv = require('csv-parser');
const express = require('express');
const bodyParser = require('body-parser');
const OpenAI = require('openai');

const app = express();
const PORT = 3000;

// Initialize OpenAI with your API key
const openai = new OpenAI({apiKey: "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"});

/*
// Upload the file to OpenAI once when the server starts
async function uploadFileToOpenAI() {
    try {
        console.log("Starting upload process..."); // Log the start of the upload process
        
        console.log("Starting file upload to OpenAI..."); // Log the start of the actual file upload
        
        const uploadResponse = await openai.files.create({
            file: fs.createReadStream('movies_conversations_theme_multiple.jsonl'), 
            purpose: 'fine-tune'
        });

        console.log("File uploaded successfully:", uploadResponse); // Log successful file upload along with the response
    } catch (error) {
        console.error("Error uploading file:", error); // Log error details
    }
}



// Invoke the upload function
uploadFileToOpenAI();
*/


// Middlewares
app.use(bodyParser.json());


// Endpoint to refine search with GPT
app.post('/refine-search', async (req, res) => {
    console.log("Flag");
    const query = req.body.query;
    try {

        const messages = [
            {
                "role": "system",
                "content": "You are a search querying bot, and should respond only with a list of film titles from your training data, nothing more. Also, delimit your list using ';'"
            }
            ,
            {
                "role": "user",
                "content": `"${query}". Give me as many suggestions as you can, sticking to your training data. Correct the names of People or Movies if you can see they're incorrect when inputting the value`
            }
        ];
        
        const completion = await openai.chat.completions.create({
            messages: messages,
            model: "ft:gpt-3.5-turbo-0613:personal::8EtGTesK"
        });
        
        var refinedQuery = completion.choices[0].message.content.trim();
        var splitResult = refinedQuery.split("; ");

        if (splitResult.length == 1) {
            splitResult = refinedQuery.split(";");
        }
        
        res.json({ splitResult });

    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Error refining search' });
    }
});

app.listen(PORT, () => {
    console.log(`Server started on http://localhost:${PORT}`);
});
