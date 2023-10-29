import openai

openai.api_key = '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'

response = openai.FineTuningJob.create(training_file='file-lnkq7IAuc0icUsoUrseT0dki', model='gpt-3.5-turbo-0613')
print(response)
