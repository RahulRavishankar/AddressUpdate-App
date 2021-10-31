## Address Update Aadhaar

### Team Reference ID: W0eNDWo2FC

#### Team Members:

1. Rahul K R
2. Shubham Gupta
3. Gaurika Poplai
4. Prithwish Nag
5. Rikesh Kumar

### Theme 1: Problem Statement 1
Whenever a person moves to a new location, he/she could land up in a situation, wherein he/she would need to provide his/her updated Aadhaar to apply for a Broadband connection. When there is no supporting documentation to prove his/her current address, as per the current policy, Aadhaar requires a supporting Proof of Address (PoA) document or an Introducer who can lend his address to update the person’s address in Aadhaar. We intend to solve this problem.

### Tech Stack
Frontend has been developed using ReactJS and Axios.
Backend has been developed using Java SpringBoot.


### API Usage 
**Auth API** - This API will be used to authenticate the Requester/Introducer while logging in.

**OTP API** - This API will be used to generate OTP for Requester/Introducer whose mobile number is registered with his/her Aadhaar number. The generated OTP will then be used for authentication.

**Offline e-KYC API** - This API will be used to retrieve the address of the Introducer from the CIDR. This will then be previewed by the Introducer before giving his/her consent.

**GeoCode API** - This API will verify address provided by the Introducer and modified address by the Requester. If the distance between the two addresses is less than 20 metres, verfication is successful.

### Architecture Diagram
![Architecture diagram](https://github.com/RahulRavishankar/AddressUpdate-App/blob/master/Capture.PNG)

### Project Demo

Link to project demonstration [Link](https://drive.google.com/drive/folders/1UP_wLIwqjsJUCy2bhsR1-ev7WQ7uzLQ3?usp=sharing)
