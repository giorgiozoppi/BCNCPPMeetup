import http.client
import json
conn = http.client.HTTPConnection("127.0.0.1",8080)
userName = input('Please insert your username: ')
passWord = input("Please insert your password (4 characters with lowercase,uppercase and numbers): ")
#payload = "{\"username\":\"jzoppi\",\"password\":\"lanocheMeConfunde77\"}"
payload = "{\"username\":\"%s\",\"password\":\"%s\"}" % (userName, passWord)
headers = { 'content-type': "application/json" }
conn.request("POST", "/users/sign-up", payload, headers)
res = conn.getresponse()
if (res.status == 200):
    print("User registered with success")
else:
    json_string = res.read().decode('utf-8')
    json_obj = json.loads(json_string)
    print("Registration failed : %s " % json_obj["message"])
