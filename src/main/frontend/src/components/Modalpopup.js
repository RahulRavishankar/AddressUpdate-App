import React, { Component } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Ekycmodal from "./Ekycmodal";
import axios from "axios";

export class Modalpopup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      ekyc: false,
    };
    this.saved = this.props.saved;
  }

  declineApi() {
    const options = {
      url:
        "http://localhost:8080/introducer/declineConsent/" +
        this.saved.uid +
        "/" +
        this.saved.requesterUid,
      method: "GET",
    };

    axios(options).then(
      (response) => {
        console.log(response);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  acceptApi() {
    const options = {
      url:
        "http://localhost:8080/introducer/provideConsent/" +
        this.saved.uid +
        "/" +
        this.saved.requesterUid,
      method: "GET",
    };

    axios(options).then(
      (response) => {
        console.log(response);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  handleoptions = () => {
    console.log(this.saved);
    if (this.props.option === "Decline") {
      this.declineApi();
    } else {
      this.acceptApi();
      this.setState({ ekyc: true });
    }
  };

  render() {
    return (
      <div>
        <Modal
          show={this.props.show}
          // onHide={false}
          dialogClassName="modal-90w"
          aria-labelledby="example-custom-modal-styling-title"
        >
          <Modal.Header>
            <Modal.Title id="example-custom-modal-styling-title">
              Confirmation
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>Are you sure you want to {this.props.option} ?</p>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="secondary"
              onClick={() => {
                this.props.hidemodalcallback();
              }}
            >
              No
            </Button>
            <Button
              variant="primary"
              onClick={() => {
                this.handleoptions();
              }}
            >
              Yes
            </Button>
          </Modal.Footer>
        </Modal>
        <Ekycmodal show={this.state.ekyc} saved={this.saved} />
      </div>
    );
  }
}

export default Modalpopup;
