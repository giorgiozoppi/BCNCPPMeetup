import http.client
import json
conn = http.client.HTTPConnection("127.0.0.1",8080)
#payload = "{\"username\":\"ingenico\",\"password\":\"LaNocheEsBella77\"}"
payload = "{\"username\":\"ingenico\",\"password\":\"Minnie1977\"}"
headers = { 'Content-Type': "application/json" }
conn.request("POST", "/login", payload, headers)
res = conn.getresponse()
print (res.status)
conn.close()
authToken = None
state = res.status
if (state == 200):
    authToken = None
    if (res.status == 200):
        conn = http.client.HTTPConnection("127.0.0.1",8080)
        print ("User %s logged successfully"  % ("ingenico"))
        authToken = res.getheader('Authorization')
        destinationIdentifier = "5bd5f758b86835f98496a3bd"
        sourceIdentifier = "5bd5f758b86835f98496a3be"
        payload = "{\"sourceId\": \"%s\",\"destinationId\": \"%s\",\"amount\":\"%d\"}" % (sourceIdentifier, destinationIdentifier, 500);
        headers = { 'Content-Type': "application/json", 'Authorization' : authToken }
        conn.request("POST", "/transferservice/transfer", payload, headers)
        res = conn.getresponse();
        print(res.status)
        json_string = res.read().decode('utf-8')
        print(json_string);
        conn.close()
