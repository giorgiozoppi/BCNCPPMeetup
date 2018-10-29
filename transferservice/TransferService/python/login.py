import http.client
import json
conn = http.client.HTTPConnection("127.0.0.1",8080)
payload = "{\"username\":\"giorgiozoppi\",\"password\":\"LaNocheEsBella77\"}"
headers = { 'Content-Type': "application/json" }
conn.request("POST", "/login", payload, headers)
res = conn.getresponse()
conn.close()
authToken = None
if (res.status == 200):
    conn = http.client.HTTPConnection("127.0.0.1",8080)
    print ("User %s logged successfully"  % ("giorgiozoppi"))
    authToken = res.getheader('Authorization')
    print(authToken)
    authHeader = {'Content-Type': "application/json", 'Authorization': authToken }
    print ("Asking for transfer")
    conn.request("GET", "/transferservice/giorgiozoppi", None, authHeader)
    res1= conn.getresponse();
    json_string1 = res1.read().decode('utf-8')
    print(res1.status)
    print(json_string1)
    print ("Asking for user information")
    conn.request("GET", "/users/giorgiozoppi", None, authHeader)
    res = conn.getresponse();
    print(res.status)
    json_string = res.read().decode('utf-8')
    json_obj = json.loads(json_string)
    identifier = json_obj["username"]
    payload = "{\"name\": \"%s\",\"balance\": \"3000\",\"userId\":\"%s\"}" % ("JoZoppiAccount", identifier)
    headers = { 'Content-Type': "application/json", 'Authorization' : authToken }
    print ("Creating a new account")
    print(authHeader)
    print(payload)
    conn.request("POST", "/transferservice/create", payload, authHeader)
    res = conn.getresponse();
    print(res.status)
    json_string = res.read().decode('utf-8')
    json_obj = json.loads(json_string)
    sourceIdentifier = json_obj["identifier"]
    print(json_string)
    print ("Asking for user information")
    conn.request("GET", "/users/ingenico", None, authHeader)
    res = conn.getresponse();
    print(res.status)
    json_string = res.read().decode('utf-8')
    print(json_string)
    json_obj = json.loads(json_string)
    identifier = json_obj["username"]
    payload = "{\"name\": \"%s\",\"balance\": \"100000\",\"userId\":\"%s\"}" % ("IngenicoAccount", identifier)
    headers = { 'Content-Type': "application/json", 'Authorization' : authToken }
    conn.request("POST", "/transferservice/create", payload, authHeader)
    res = conn.getresponse();
    print(res.status)
    json_string = res.read().decode('utf-8')
    json_obj = json.loads(json_string)
    destinationIdentifier = json_obj["identifier"]
    print(json_string)
    payload = "{\"sourceId\": \"%s\",\"destinationId\": \"%s\",\"amount\":\"%d\"}" % (sourceIdentifier, destinationIdentifier, 500);
    headers = { 'Content-Type': "application/json", 'Authorization' : authToken }
    conn.request("POST", "/transferservice/transfer", payload, authHeader)
    res = conn.getresponse();
    print(res.status)
    print(json_string)
    json_string = res.read().decode('utf-8')
    print(json_string);
    conn.close()
