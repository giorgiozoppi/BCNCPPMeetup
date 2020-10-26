#!/usr/bin/env python3
import boto3 
#Explicit Client Configuration
polly = boto3.client('polly', region_name='us-west-2',aws_access_key_id='AKIAQ325O4IYNIGXB36X',        
aws_secret_access_key='4bp14Q2MF0KbtX9XQ7RVSRhCmNiBYn/0Jfsb0FrO') 
result = polly.synthesize_speech(Text='May the wind always be at your back and the sun upon your face. And may the wings of destiny carry you aloft to dance with the stars!',OutputFormat='mp3',VoiceId='Aditi') 
# Save the Audio from the 
audio = result['AudioStream'].read()
with open("blow.mp3","wb") as file:    
    file.write(audio)
