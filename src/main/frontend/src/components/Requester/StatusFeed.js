import React, { Component } from "react";
import AddressVerification from "./AddressVerification.js";
import AddressEditForm from "./AddressEditForm.js";
import ListGroup from "react-bootstrap/ListGroup";
import axios from "axios";

class StatusFeed extends Component {
  constructor(props) {
    super(props);
    this.state = {
      // Address index
      addressshow: -1,
      // Addresses and Status
      addresses: [
        ["Address 1", 0],
        ["Address 2", 2],
      ],
      edit: false,
      xml: "null",
    };
    this.orgAddress = "";
    this.saved = this.props.saved;
    this.getAddressesApi();
  }

  getAddressesApi() {
    console.log(this.saved.uid);
    console.log(this.saved.txnid);
    console.log(this.saved.otp);
    console.log(this.saved.requesterUid);
    const options = {
      url:
        "http://localhost:8080/introducer/fetchAddress/" +
        this.saved.uid +
        "/" +
        this.saved.txnid +
        "/" +
        this.saved.otp +
        "/" +
        this.saved.requesterUid,
      method: "GET",
    };

    axios(options).then(
      (response) => {
        // console.log(response.data.eKycString);
        // this.xml = JSON.parse(response.data).eKycString;
        // console.log(this.xml);
        // this.xml = response.data.eKycString;
        this.setState({ xml: response.data.eKycString }, () => {
          this.orgAddress = this.getOrgAddress();
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  resetAddressShow = () => {
    this.setState({ addressshow: -1 });
  };

  enableEdit = () => {
    this.setState({ edit: true });
  };

  disableEdit = () => {
    this.setState({ edit: false });
  };

  editaddresscallback = (newaddress) => {
    var addrs = this.state.addresses;
    addrs[this.state.addressshow - 1] = [
      newaddress,
      addrs[this.state.addressshow - 1][1],
    ];
    this.setState({ addresses: addrs });
  };

  feed = ({ status, index, address }) => {
    const row = {
      marginBottom: 10,
      height: 60,
      padding: 10,
    };

    const entryStyle = {
      height: "100%",
      display: "flex",
      alignItems: "center",
      fontWeight: "bold",
      float: "right",
    };

    const description = {
      height: "100%",
      display: "flex",
      alignItems: "center",
      float: "left",
    };

    return (
      <ListGroup.Item className="bg-light" style={row}>
        <div style={description}>
          <span>{address}</span>
        </div>

        {status === 0 ? (
          <div className="text-danger" style={entryStyle}>
            <span>Yet To Approve</span>
          </div>
        ) : status === 1 ? (
          <div className="text-warning" style={entryStyle}>
            <span>Consent Approved</span>
          </div>
        ) : status === 2 ? (
          <div style={entryStyle}>
            <div
              className="btn btn-outline-secondary"
              onClick={() => {
                this.state.addressshow !== index
                  ? this.setState({ addressshow: index })
                  : this.resetAddressShow();
              }}
            >
              <span>Address Processed</span>
            </div>
          </div>
        ) : status === 3 ? (
          <div className="text-success" style={entryStyle}>
            <span>Completed</span>
          </div>
        ) : undefined}
      </ListGroup.Item>
    );
  };

  getAddressComponent = () => {
    var lists = [];
    for (var i = 0; i < this.state.addresses.length; i++) {
      lists.push(
        <this.feed
          status={this.state.addresses[i][1]}
          index={i + 1}
          address={this.state.addresses[i][0]}
        />
      );
    }
    return lists;
  };

  getOrgAddress = () => {
    var parser = new DOMParser();
    console.log(this.state.xml);

    var x =
      '<?xml version="1.0" encoding="UTF-8"?><KycRes code="15bec7eca1364794aef7d01766185dbe" ret="Y" ts="2021-10-31T19:02:39.404+05:30" ttl="2022-10-31T19:02:39" txn="UKC:3ebbf59e-438a-4534-b248-da3c49f974f1"><Rar>PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48QXV0aFJlcyBjb2RlPSIxNWJlYzdlY2ExMzY0Nzk0YWVmN2QwMTc2NjE4NWRiZSIgaW5mbz0iMDR7MDEwMDAwNjhhR01ia1VyaXk4YnUyaUNkcHcxQlNPNnNzQnZBK3krUWRZUjlwaXJIQTZoZ29QbGsrVG9DWkRYM0NUYjM1elNqLEEsZTNiMGM0NDI5OGZjMWMxNDlhZmJmNGM4OTk2ZmI5MjQyN2FlNDFlNDY0OWI5MzRjYTQ5NTk5MWI3ODUyYjg1NSwwMTAwMDAwMTAwMDAwMDEwLDIuMCwyMDIxMTAzMTE5MDIzOSwwLDAsMCwwLDIuNSwyMGVmMGYwYzhkMGVlYTk4NzcyNDEyY2VhOWIzYjkyNjEyZTNlNTNjYjVlNTkxNTJiNTcwMzE2NWY1NmU4YTUzLGVmYTFmMzc1ZDc2MTk0ZmE1MWEzNTU2YTk3ZTY0MWU2MTY4NWY5MTRkNDQ2OTc5ZGE1MGE1NTFhNDMzM2ZmZDcsZWZhMWYzNzVkNzYxOTRmYTUxYTM1NTZhOTdlNjQxZTYxNjg1ZjkxNGQ0NDY5NzlkYTUwYTU1MWE0MzMzZmZkNyxOQSxOQSxOQSxOQSxOQSxOQSxOQSxOQSxOQSxOQSwsTkEsTkEsTkEsTkEsTkEsTkF9IiByZXQ9InkiIHRzPSIyMDIxLTEwLTMxVDE5OjAyOjM5LjI4MiswNTozMCIgdHhuPSJVS0M6M2ViYmY1OWUtNDM4YS00NTM0LWIyNDgtZGEzYzQ5Zjk3NGYxIj48U2lnbmF0dXJlIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIj48U2lnbmVkSW5mbz48Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnL1RSLzIwMDEvUkVDLXhtbC1jMTRuLTIwMDEwMzE1Ii8+PFNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNyc2Etc2hhMSIvPjxSZWZlcmVuY2UgVVJJPSIiPjxUcmFuc2Zvcm1zPjxUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjZW52ZWxvcGVkLXNpZ25hdHVyZSIvPjwvVHJhbnNmb3Jtcz48RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjc2hhMjU2Ii8+PERpZ2VzdFZhbHVlPlV4WDl6clNCTUl6RVhKYVJJZ08xeDFvQ0krWk9kdktuMFBwQnYvN1dscTQ9PC9EaWdlc3RWYWx1ZT48L1JlZmVyZW5jZT48L1NpZ25lZEluZm8+PFNpZ25hdHVyZVZhbHVlPkF1TXNqRWMxMFIwWVQ5QXpWZTdFWCtiVjUvWm1BdHhXTHk2Z2E4cWhTb1FjbGdIQlV4cTYyaUVFSDNRY0dsY3RWOVp6bGNSN2lyS28KY09JSXRTQW52S2htckxoS25neFpFbjdXUlFhUDlFSStqUHJ4OGFib3NlZnhKV05icjlFTFdodjZKWHdsL2I5MjJjSGY0ekEybUVOdQo3WFhhOGc1SUdsVUd1ZGxyb3N1bWhreC9IV001T2lGY05McmNlV3FjWE01WnJTOEdFdzI5ckdMOGx3YXlCNWpWaVdRL1YvNzdFVHplCjlZbVFWcThwQlJmNWpzaUtHSGR1VGJMa3RZT0ZDaW1IQkp4a0VZQS92ZjlYcFZERXorclJtSXZJZ0pLZEdKeGkxVVhGU0hVTENmWTQKb0tJTGp1KzRGcmpPMFRlNktGeXhmWHExYXBuM1JMSzlrSTRpNXc9PTwvU2lnbmF0dXJlVmFsdWU+PC9TaWduYXR1cmU+PC9BdXRoUmVzPg==</Rar><UidData tkn="01000068aGMbkUriy8bu2iCdpw1BSO6ssBvA+y+QdYR9pirHA6hgoPlk+ToCZDX3CTb35zSj" uid="999920778255"><Poi dob="15-12-2000" gender="M" name="Prithwish Nag" phone="9619360141"/><Poa co="S/O: Manoj Nag" country="India" dist="Bengaluru" house="No A-104 Purva Panorama Apartment" lm="Near Meenakshi Temple" loc="Kalena Agrahara" pc="560076" state="Karnataka" street="Bannerghatta Main Road" vtc="Bangalore South"/><LData/><Pht>/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADIAKADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwC8BUgFNUVIKCBpFJtwakxxilAGKAAfdFNJp+MU00AJ70uajkkWNC7kBQMkntXM6h41tLC6MSqJk/vxNnH9D+dAzqwacK86n+IlykuYbaNox13oQT/49x+tPj+JikAyWZVu67uMex9fb9aVmFz0KiuLtviJpzj96kiHPcV0djrmnakqm1uo5M9QDyD6EdRRYRo0tMjkWQsFOShwR6GpMUANNBpxFBFADKMYpxFGKYxtNI4p5pCKBESjipF4pqipAKAExTgKUClxQA1sCue1nxVp+mRuFmSaccBE+b8yOlO8Uata2thNbu58xhgqOCfpwa8jklAJ5JBPFNIDQ1PWLjVrtpp5sJniPJ2j6VmyXIDAA5pCokGOp9KDGifIACT6DpTGO3LKPlJH1qnMuxu/PrVtiEHMmweuKrytE/Ri/vSuOxX3Ekk4qSG4lt5BJFIyOOhU4NRuADwKYcCncVjvPDnjqWHUY11Jg0cihJJcDPBOCfpk16pFIkqB0IK+tfOKn/8AXXaeE/GD6ZIttdyEwMQMnnaKTXUVj17AxQRUVvPHcQpLG4ZHGQQc5qakAzHtRjipMUmKAIyKQipSKYRTAjUVIFpqipBSAUCgjFKKXGRQB5Z49lkku1LAKFOAC4yfw64rid+9uMA/rXQeMZAdfu0UEBHI5rmkjLHIGfrVdBonZtrbuf5ZqdIpp87I3JPetHSdKEuJZ8HPIGOK6aC2jixhRXLUxCi7I6adBy1ZykXh25uWBKnHc5q5/wAIowTHGRXYQhB2qyVDNwuDXI8TNs6Vh4pHAXHhvyIGYE7sdKzLjRZEh3qCT6V6JdRbgQV4qhLGpi2ba0hiJdSZ0F0PNthViCORUiJubJOK3tS01FnZhwrc/jWO8TRMR1ruhNSRxTi4s774e6xIkz6fPMXjI3Rgn7vqB7V6SrAjrXgOl332LUYLjJHluCcdcV7fZXkc8EciOGVlBUjuKpkGnjIpdvtTEYGphzj1pCGYphWpsUw00BXSpRUCmpFNSBIKUGmZpaBnhniQs+u6gWbObh+R9TWbaxb3AJ6nAFbHiu1+zeIbobdieZlRjAxVHTUJvkUD3JqpOyuOKuzrbOLYijsBWlDAXI5AFZ8EEtwAFOyMfxHua1otEMyhjduoHqa8uSTerPRi2lojTtNNDKDnmrq6dh2A6DvXPRRPYSHyrl/fng10mnagzx7SoY/zrJwS2ZrGTfQrT6aGXHrWFd2YjYruH51p6hqMpJQSbTnqtYy6bBO5ee52k8gs3JpwhHdsUpPojMvLVcfNg1y2ow7JMDNdlcaZblWWOd2x33A1zGr2skPDncF5DeorsotJ2TOWsnbVGATtO49c9q9R8JXpOjWwyeARz9a8tlzkL2Ga9A8NK0GkQZPXLD8a657HIlqei20+9RV5GzXO6fPnHNbsTZA5qUxMsU004HimN7VSEUkPFSioU6damAoGPBzThTQOKcOlIDzz4jaWSYb9R8v3G+tct4dgE148h6KAK9C8YsbmJNPCja+HZj6A9vyrkdItI7O+uokzgbeCc9qyq1FytdTenSldSexpXMs8SBLdMt6+lUNSstQZYGsNRnneRCJo490ZU+nOMj8T0rpre0EigkVqW1jFD87Jk4rgp1VF7HbOlzLcwY9PEMNsyGdiEX7RvxtY4+Yqev5j8qmsp2ilZQ+F5wTWpfjMJJPToBWfp1g91IAO5wo9ampJTZpCPKZlzL/pwycjk/Wo9T0pptNhe0eb7UH3Sl3CRuuOgw2ev8+vAq5rWnS20pGMSJ1FW9PL+QONynqD0/CrhPkVyJw5nY5I2MtvYweXcTvejJk5JTrwOe+KfPDJcWh89RvxXXy2cbklVxWTqFt5akD0qnW5pEukoxPORZyS3piRSQGwT2XmvRLeNYbeONTlVUAEd65W2VF8QKrICJB0IzXVQjaCo6A4Fdjnd2OP2do8xp2EmGxXTWrZQVylmf3orqbIZUVcTKRfXpTWp6rimuKtEFFBUwFRpUyjIpDHAU4CkFPAoA5/xDZs7JdDlVXYw9OeD+tcs1usOoCVf+W0alue44/lj8q9HljWWNkYZVhg/SvO71ZIpI3KnarlCTwR+FctaFpX7nbRqXhyvob9jJhQB1rRLY6msHT5wGAJrTkuAqZ9a82Sszui9BNQnEcI43AkbhjPFYqXGoPetMk1uIVHyxqjBvzJx+lS3Mxdto5/GoI0jBy0oyPQVrBO2xMmRX82oyziYyRRqpwwkQuWHtgir2kXu2eQOMQNjbkY57nB7dPyqrdTRMNpbBHfFV4ZQDgkEHowq2nbYi9mdHOy7jgce1Y+oP8AKT7Ui3LKwBbIqpqMoETEenFRCL5iptWOagw3iGH1ANdWvArn9LspJLtrthheArZ5963xXpJannOWli/p8ZeUYFdfZwlUGRXPaFEZZsLj3JrtLWyeZ1VQSPUCtYowkyAxkDOKikHGa2p7HyV2ucH0IrJnXazLkcHHBqibmclTLUCVOtSUPAqQDApi9aeBQAEcisHxHYwy28m2JBJjduCjOa3wORVbWLcq4BHVQfzFDVwTszgLSb5VOavySGSMHdWXfRnTr8p/yzkJK/1FSxzZ4zxXm1qdpXPSpVLxsE6TzfKj7Y++0cn8als9PglYJJK8YPfbn+tG8DGOtSB7hlwq5HstCm1oaRsncbfafaIiCOSSQkfNnIrKSyMUwZZHT1Gc1qOk/wDdIP0qnJlW5JJqlNiqWl0HnAGc9KytRusxle31qxdXG2PGRk9azbWA318FPMSfM/v6CtKcLu5hUnZWNqxi8qyiU9duT+PNWR1pKcv3hW5ynSeHIpmkyillz0Ar13RNOnWLfKdm3hV4P19q4fwPNbWxhMygo4zk9uSP6V6ZJfwRqFEilj0Ga3jsZOzepU1q3U2DStgsn8R61wE4wxNd5rzyNp6oBhWwW/wrhbojeeMUxddDHQ1OpqojcCrCnpUFFhacDz1qFW6U/NAEyHLCr/iW28uW3bqJLeNv0x/SsxGwwre8QxvcxaYIVLsbVOlNCZ5R4qtt/lYHOSQfyrm4bx4mKSDDL+teheKICmlQrtDbLp1LjP3sYK9McFT3ribqyEyZAwwrlm1ezOqmny3RatrmOTBB59K2oryMJtCjpXASC5tJMqTUia7PGMOOfesZUG3eJvGslozt5bpFHAB45rC1C7jjQ8jNYNx4glZDg4+lZUt3cXT4XcSaqGHd7sU66tZF671DJIByx6Vp+FLe9u7m5WKLeqxGWQ9wF/8A11nafpLH95N96uv8DSpD4saMoHi+ySAxnoSSoB/U/nXSmvhRzyva7G5p0Yy4qa6tJIZmwpK9cj0yR/MUy3GZBikQdTorNGoXdgYxXV2F64u4S/7xdwG0nrz0rldP+UA+1bED4YVrF2MmdbruqGdVj8opsznLZrk7hyzE1cuLpph8zZrNmfmrEjNQ1YQ1Vi5AqW7f7DaC4lwqltqgsASevTrUbFpXdkWQaiN5AlvPOZFMcOAxB6seij1P8qzonn1A7AYjvzhTwqqO7H0HU5HYdmqTSrCLWtZHkRH7BZgbVc/ePqfdiMn6YqXI0VPua+mI8gWS9DAnny4scfXJzmtbVbvUbLTDPBHFDkrDB5vVSxwCOSMjJPI7VoBoreAmeeOGJF3FVHIA5/H8qZ4gtDLpkUk00hjimQ7CBlyx2jPTj5un0oRF1cxNYsDL4OuLUyKJIwroW+87r2HqSBj65rzhSHXmvTpJ2NrGyuXnVP3w7bsAN+OQf8mvPtatBpurtGNvlzjzI9qkAf3l6Dpnt2IrCtBtXR0UJa2ZnT2aTKQQD+FZk+jJIB0z64rbVqViCP8A61YRnJG8oJnMtoa55xj2FWrXSY4ju2itY80jcVTqNiVNIpTlYkIUYFT/AA+uGj+INngZ83dGfptJ/oKq3eX4ApPC88mneM9OljjLyFwqoOp3ZX+ta0jGrrseqatZsviVYIoYj9pjLhiSrKwIyQQD2A46HnNYt5pYS6mR2lS9yWJaMsjDPqOmeMDP4VoeIbye9nsdyzWlwsyhXTKthvl9vX15rn9aj1W1uFifUZjKMMFd2yFzjnqo7cZrdmUI30ua0Uttbwxs1ygyAG3/ACEE9sE/lWhDIDgqRg9CK41dUvLa68oyw3DTLuO0tkH1JGB+PvUza7qcYDNBEiIcMQpPH+1yT7fjQmN0vM7CSX3qvI2ax7bxFbXAG/K8Z3AEr+vP6VoCeOQKUdTnpg1aZk4tG3/YE0KwgQFmYFldWwAeMbjz6k9O1cTrUv8AaOsNFDNi0txsUueoUfM2fUkHkdeK9RS/ij04rcqJpHTa5WIoG9a5/UJLK30y6W2sbWEGB8FW+bAGeeM1mzWnKzOLW0uBZyGB1Z2dY3aJgdiE4Az1G4/oK1vCreXqMOnpFukly8jO2Bjbkdv85NZui6rJFa6iFhBGYH3AngrJx29zUegXzt4hgk3uhEQGUHomPWl2N3HeJ3fiiOO01PRFZYYYHZo5FAxlWaNW5+hp/im5ibRbmKzdmZQJPNZi+NpDHbuz6fSsPX55rnVbJY4nZoZ4vmyx+9IPfj7lbUUUcjbGkWe4cMNpUGMZBzknjpn8as5raK5l6O32lLmSZysQuGw3UybjvyB9H/zmua8VapbeItXi0vSYle6tdzqw6u/A2bj95j7cenfGrpsz2FnqeoTMHe2tUdQc7Q6h1CfiygcetcNo1wunW8N19lUzwyeaDuYEkGPr+o/Gk0Wld6F6NWZAcFTjlWGCKkEMh7Gtq/vYdYv7a+jjVUvIiSVGCZFYhty9jgrn1znvUyWijB71wTXK7HZT95HPm2cDmmGI9K6WS3Q9agNosfKj86jmL5TAWyJ5ZayI2Nr4vicytGsTodyclBgHiu0EXmOFxj1PoK4nxHJHP4hvZrOJ0iwuwEngbVGfoe31rqw6cm2c1eSjY9N1OKQ6dcJbXJdFAdI3GGDeoHIz06HNZPiO/e6tbO8Mkf71NpAUDORu7E45Xv61DYR+ILGwj+32j3Vk6D9+I2Dxgc5z/EPUnJ96dOIV8MTwfZJHuLSUqDvyFKvjOMeme9dUloc9NrnRU8PamW1dbK6ija2uSGZQ2MEfNkEHjgEYHXpXSto1ldzXDx311aQqxVT5TDccdS4AI/P365J4Kxkhj1K0lHmRKJQu4DJK5GfTsa9BsYBOZo7ELg/MVmjjDehIzyRn+dQtjWqrNNGFaWN/eSSWP9uAgFwRJdSbDhsEYxjGex6g9K138NzR2LPcxxmRD/rIcgAgjJA4256/3Rz2wDlWFpqEfiAxBFdpLm4X+FsAqH/kK6SCe4snidSg5Iby5Ad2PUZP8qdkRJy6FsxoqD7TcRqAOEjALH644/OsrXLmyTS7gBJlYxsoO4MORj0961Io7GNM7ZZpOwICisbxM8w02dJrdBgKBlNpHzD0qXYcLuSRx+n/AGdLHUGDuzAR7QE/2s9foDWr4US5bxHuis0dVV2CGPOB0/rVPSLaaeGVY4kjV541Zsc45B/9DWtbwNpdxca9IscoXELEnJ6ZAP8AOl2OiW8jS1+F316E3dzGmDASoOePNYdBx3rp7OCO4ljt47ZVicgGQ5L/AJ9B+VYvinSVt9RmG4lkskkH1Epr0OK3T7EsSZT5cAqcEfStGcbaseZ3Wm317eX2lWUbLZx3Be5ZgAWKyMyrk9eqtx6CuLurO6t9QvYTIyqGdCC/ocgfmBXvENnBZWXkwR7UAJPck9yT1JPqa8k8UwC31u7GMguJMf7wDf1qZGtGV73M/RLXUJIp7gQm4itDvaMMAfmBGc9eMdvT2reUh1yMgjghhgqfQj1q94Ytf7P8T3uk3D5WWF4eR9/oQfxXJrpNZ0u0vreea1A+2RRCdQnV0OcA+vQgVlOnzR8zV1eWfkzjApPegxZqZNkkYdTkEZp4wDXHynVdEMNoVKzyReZb7thi25Ep67T7dyO/fjIPAapbS6n4kigtQyyXVwzlWYjYC7KiHnnAAOf9rHavTtZ1aDStMOmw7ZrzYRK3BWLPJA9T/h7YGDp2m29hrcckjhmjS1ud2c8GZtwJ7kZA/CvQorlVjgra+8zs7PQNUt7aJPtfzBACVkcdvpWFY2F+JtU0wfdGA5AUsAy4PJ55xn8a9QUfvce1URYxxarNdAtumRVYHoNuen51o9jnUzwCe2uT8suJJDjPT5QCRg/pXatKJ5rWSGzAEuHJUN8iMjcZz6xj86p+JrX7Jrl9DwD57SFgOzgOq1s+FpBJoGpvKeLSNZECnBIQvJj8Tn86iO511XeHMYkqWMfiEgXZAW6RfmiORvjxnH1rXWGdNTjbTZnk2wyb/kwGBK9j178Yq94g0O3l1KxvRGA815BHNg45DHB/I/oK0dU0S2SxMsQ2SI67Tk9Scc/nVXOdtNGxbadaRhSIkLH15/nWF4uCx6FO5HMkqqP5/wBK2tGkM2mQTseWhUj8hWD4/kWLQIkJxmcf+gtUSRVNvnRy+lx+TbWpDZ84mXHp++jX/wBkrU+G4267Jz1tm/8AQlrJa9iRdLWJJGC2sedq9/N3GrXgO/MGtuwhJPkEYLBf4l9aLbGt7qTOs8ZqDeXBP8WnMPyf/wCvXXwEbVHtXn3jDUZZbs7TCM2kq4UM56jiulitbu43B4snrmaY4P4CtLHK9jRu7iGFWDyohx0ZgK8m8ZTw/wBtylJN26ND8vP8IH9K9IudKkVAfMhjOOQkWf1NedeNbLytViLyu+63DHsOpHb6VEjah8Q4aqF8VWl8H2ljAGLqf4kAP6ZrubjUdkMJ0+SBXEToAx/uMAucc45b868+vLOGCS0lUuu+yhlBDnhxExHX3UV3sVmrX0ES3LNFI/3gqklWRjnOPVadiZyvY5a6WK1v3t4ioQ4ZFHYEcD9CMe1VNQuTaWzSKwWTgLn1/wA8/hXQ+LNHe1v7a4824l3xtGDkYyvzqOncbxXM3FvPPLHOq71iBZFBI245Z2I6KB1P5cnK4SpXkrHRTrWhqP07TWieGe5ilmupH/cW6nDO3ue2OpP8PfngVZgsMrG8uFaSWC4jV05VikobIx2JLGlTVzds1nYB3kkbbJfY2Box0VUA+ROBwTznkZxUiaYbo299HK8dgrmyi2t99mRizDPbIx74PToOiK1MpttXkenW/iDT7iOK4SZjG6hgfLboRn0qSXVrMTIxkYL/AHjGwH8qxtAsIp/D1hKl5dH9yqH950K8HHHqDWtPpkapHie4zuHzGTJps5zzvxvdWT6w0kL7keFJZcKckhinH/AcVU8LanbC01e0LMJLi1IXKnHQgg8f7Q/Ktfx9pcUH2WY3E75Eqku+ewK9vUmsDwmm7xKi+ZKqzhwwV+gwW9PYVHU61Z0jv/EjGTRJLm3+8DHJkdgHDZ/DFX5J0u9CluIhvOxjtB6Mvb65GK5W909otIukiFv5UUMkW8Mwb5QRn0zxUPh3U7230xyAxinkdwZULAcnoR71bRypaGtoNvc3+g2UrRwldmFy7A4HFc744jMM0EBKKwBY7CSfQcn6Giioka0vjKT2UWzTN7O+bWNjlz6uf6Vc8Ax2i68/niLb5DH95g919aKKfUtP3ZHT+NriPzY9qOc2dwv3COw9cV08Etw0mNsScdSxY/lx/OiirehzdBLyGd48m4bPoiAD9c15n48tttzayO7tuiK5ZvQ57fWiis5M2ofGZN9NYzWdjs8hT9iQMRgHcodT+uK6fwvfR3Ojxz+a/mW+xHxIeFRs8enBx+FFFO+o5RXKzqPEtpC2gzzLI5kgAnG+4Y/dOTjnuAR+NeaX94JHewtmCWq4M0275dq9AT1Cg847s3c7aKKG9BUVqSabp5vvLjj3wafK8fmyAbZJw2QMZ/h3HH+ee28SaZBbeHYCHkEdpNE4AfaAN23gDH96iiq2M5NuV2L4Xitv7D8rzHzBPJGw89gM7yemfQitmcWYtQxkXd1BaUk/gSaKKGS9zl/HAtTpUEocsROhOZixAw2eCfpXBaPMsGvWLtJs3SIv+sI+XO2iioe51UvgO41sxQaLfJazqQyyhtzF8sVL8c8darWEj2vh+1haMnynZWdMYJ3HPGc9TRRWi2OZn//Z</Pht></UidData><Signature xmlns="http://www.w3.org/2000/09/xmldsig#"><SignedInfo><CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/><SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/><Reference URI=""><Transforms><Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/></Transforms><DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/><DigestValue>Oo1Y0UDO7l2Dpr9HHbY/M2/c16ll5Do/ocKsj7G7lic=</DigestValue></Reference></SignedInfo><SignatureValue>T3TinJaLLe0RiPQJpqjX7BR1JLCO6gd5Qfzs7JWbNcJTdpcliUvy/XBh0jVdilL0O+C5q8ys4g4l\nx6u8fMOIzfWy02xCJjKNZTCmp88HkTpa6gSQJdyzk9L0hcPlw4d8AN6tEnvd8fOdMT2hMlnzu4fr\nYDA2DotHDPrdn0cX8yFnneta8ilE7gRbUrWHiXhPT0oLpp48PMAiLezZvPQPvcOqzslmm8eDmTxk\nlIo6CWJK27Jpux1XL9wujLlV3zARXPDYkehK4Z/95azqla1GUdRBVl+nyZEp73X7XSTuCm+SxATI\nHUIHiu+bNHGkPQKoYnxtLHw/9cic+mc1s7Hgbw==</SignatureValue><KeyInfo><X509Data><X509SubjectName>1.2.840.113549.1.9.1=#1617616e75702e6b756d61724075696461692e6e65742e696e,CN=AuthStaging25082025,OU=AuthStaging25082025,O=UIDAI,L=Bangalore,ST=Karnataka,C=IN</X509SubjectName><X509Certificate>MIID5DCCAsygAwIBAgIEATMzfzANBgkqhkiG9w0BAQsFADCBqTELMAkGA1UEBhMCSU4xEjAQBgNV\nBAgTCUthcm5hdGFrYTESMBAGA1UEBxMJQmFuZ2Fsb3JlMQ4wDAYDVQQKEwVVSURBSTEcMBoGA1UE\nCxMTQXV0aFN0YWdpbmcyNTA4MjAyNTEcMBoGA1UEAxMTQXV0aFN0YWdpbmcyNTA4MjAyNTEmMCQG\nCSqGSIb3DQEJARYXYW51cC5rdW1hckB1aWRhaS5uZXQuaW4wHhcNMjAwODI1MDAwMDAwWhcNMjUw\nODI1MDAwMDAwWjCBqTELMAkGA1UEBhMCSU4xEjAQBgNVBAgTCUthcm5hdGFrYTESMBAGA1UEBxMJ\nQmFuZ2Fsb3JlMQ4wDAYDVQQKEwVVSURBSTEcMBoGA1UECxMTQXV0aFN0YWdpbmcyNTA4MjAyNTEc\nMBoGA1UEAxMTQXV0aFN0YWdpbmcyNTA4MjAyNTEmMCQGCSqGSIb3DQEJARYXYW51cC5rdW1hckB1\naWRhaS5uZXQuaW4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCtnXWu8+uja+Us3z+T\nWjY1yV5KZq8I4CT9oHVk0hOMOhZz5Vash4mvj4mHa8u9y2/qZXIdIB8s006k2jz0dvnpBiMFzoJo\nQ5TSPwJl13gGKu/NTProBIELiDnOESfOFevQas48hMbHxvRIIrTUIZ+wL017uXCF/UIamdwRZ8SS\noN897tWwrRmSutpsgDCE/F4k88XzfOyx2UyG+kJJZOYIWeYWMhLRH4ascP/OE1/9BtJ31wZEZFEU\np0Saat5KNWLlDhKF4R8mwJc7+OMIOw5YPyjY/iW/OyoEwgxvjgqCizlWZnv+oRq8yBxtBkfwkakw\nxYv1rOamNbHpET30EB2TAgMBAAGjEjAQMA4GA1UdDwEB/wQEAwIF4DANBgkqhkiG9w0BAQsFAAOC\nAQEAVGhmm2h3d8aOBhoZonAN6C5W1NY0hsuKP7xZ3ZyVeEhs1/DIavaPmrNx3LISEJZ9UDwGJdP/\n6+1M86DXUK5dvyjpfQOESxnXFNqvbuQkh2C/IxawCWjQCjWgUm+yyRXnpvcgLGNYGhKxnmuZVJwJ\nOlScc/6wjqvONscPV+neHwerrbFBq8DwXGgqiJU2dijRFpChhN09PSbkQ/y2ACOBOS87XJrcxBP+\nAyBSTdQNG+q94Ww/PKBDgIvnR2JzpYA+eHqu45CJDy5zA1oHT1N7JZlm5GPe798g5GMrBfd/CZ5G\nTeGRS+MNSAGmD3BjankxWFWMVdNiXjLs400EZdKQGg==</X509Certificate></X509Data></KeyInfo></Signature></KycRes>';
    var xmlDoc = parser.parseFromString(x, "text/xml");
    console.log(xmlDoc);
    // var name = xmlDoc.querySelector("Poi").getAttribute("name");
    var Address =
      xmlDoc.querySelector("Poa").getAttribute("house") +
      "," +
      xmlDoc.querySelector("Poa").getAttribute("street") +
      "," +
      xmlDoc.querySelector("Poa").getAttribute("loc");

    console.log(Address);
    return Address;
  };

  render() {
    const box = {
      margin: "auto",
      width: "600px",
      padding: "10px",
      overflow: "auto",
    };

    return (
      <div>
        <div className="card" style={box}>
          <ListGroup
            style={{
              maxHeight: 600,
              height: "auto",
            }}
          >
            {this.getAddressComponent()}
          </ListGroup>
        </div>
        {this.state.addressshow !== -1 ? (
          this.state.edit ? (
            <AddressEditForm
              editaddresscallback={this.editaddresscallback}
              hideeditcallback={this.disableEdit}
            />
          ) : (
            <AddressVerification
              index={this.state.addressshow}
              address={this.state.addresses[this.state.addressshow - 1][0]}
              showeditcallback={this.enableEdit}
              orgAddress={this.orgAddress}
              saved={this.saved}
            />
          )
        ) : undefined}
      </div>
    );
  }
}

export default StatusFeed;
