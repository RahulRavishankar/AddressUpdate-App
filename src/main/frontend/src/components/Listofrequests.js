import React, { Component } from "react";
import ListGroup from "react-bootstrap/ListGroup";
import Modal from "./Modalpopup";
import axios from "axios";

export class Listofrequests extends Component {
  constructor(props) {
    super(props);
    this.saved = this.props.saved;
    this.state = {
      showModal: false,
      option: "",
      index: -1,
      requests: [],
    };
    this.getAllRequestsApi();
  }

  getAllRequestsApi() {
    console.log(this.saved.uid);
    console.log(this.saved.txnid);
    console.log(this.saved.otp);
    const options = {
      url:
        "http://localhost:8080/introducer/getAllRequesters/" + this.saved.uid,
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        //  'Access-Control-Allow-Origin': '*'
      },
      //  data: {
      //    "uid": uid,
      //    "txnId": "0acbaa8b-b3ae-433d-a5d2-51250ea8e970"
      //  }
    };

    axios(options).then(
      (response) => {
        var data = response.data;
        var list = [];
        for (const uid in data) {
          list.push(uid);
        }
        this.setState({ requests: list });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  getRequestComponents() {
    var lists = [];
    const row = { padding: 10, height: 60, marginBottom: 10 };
    const description = {
      height: "100%",
      display: "flex",
      alignItems: "center",
      float: "left",
    };
    for (var i = 0; i < this.state.requests.length; i++) {
      const requesterUid = this.state.requests[i];
      lists.push(
        <ListGroup.Item className="bg-light" style={row}>
          <div style={description}>
            <span>{requesterUid}</span>
          </div>
          <div
            className="btn btn-outline-danger"
            style={{ float: "right" }}
            onClick={() => {
              this.saved["requesterUid"] = requesterUid;
              this.setState({ showModal: true, option: "Decline" });
            }}
          >
            Decline
          </div>
          <div style={{ float: "right", width: 20, color: "transparent" }}>
            S
          </div>
          <div
            className="btn btn-outline-success"
            style={{ float: "right" }}
            onClick={() => {
              this.saved["requesterUid"] = requesterUid;
              this.setState({ showModal: true, option: "Accept" });
            }}
          >
            Accept
          </div>
        </ListGroup.Item>
      );
    }
    return lists;
  }

  hideModal = () => {
    this.setState({ showModal: false });
  };

  render() {
    const box = { maxHeight: 500, height: "auto", padding: 10, width: 700 };

    return (
      <div>
        <div className="card" style={box}>
          <ListGroup>{this.getRequestComponents()}</ListGroup>
        </div>
        <Modal
          show={this.state.showModal}
          hidemodalcallback={this.hideModal}
          option={this.state.option}
          saved={this.saved}
        />
      </div>
    );
  }
}

export default Listofrequests;
