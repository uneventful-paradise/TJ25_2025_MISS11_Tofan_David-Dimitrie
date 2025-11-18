import requests

url = "http://localhost:8081/demo1_war_exploded/hello-servlet"
headers = {"Accept": "text/plain"}
data = {"page": "page1"}
responses = requests.post(url, params={"desktop_request": "true"}, headers=headers, data=data)
print(responses.status_code, responses.text)