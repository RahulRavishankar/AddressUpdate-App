import React, { Component } from "react";
import { Navbar, Nav, Container } from "react-bootstrap";

class LoginNavbar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      logintype: "Introducer",
    };
  }

  loginchange = () => {
    if (this.state.logintype === "Introducer") {
      this.setState({ logintype: "Requester" });
    } else {
      this.setState({ logintype: "Introducer" });
    }
    this.props.logintype(this.state.logintype);
  };

  render() {
    return (
      <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
        <Container>
          <Navbar.Brand href="#home">Address-Update</Navbar.Brand>
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav>
              <Nav.Link
                style={{ "margin-left": "60vw" }}
                onClick={this.loginchange}
              >
                Login as {this.state.logintype}
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    );
  }
}

class GenericNavbar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      addressupdate: false,
    };
  }

  addressupdate = () => {
    this.props.addrupdtcallback();
    this.setState({ addressupdate: true });
  };

  status = () => {
    this.props.statuscallback();
    this.setState({ addressupdate: false });
  };

  enable = () => {
    !this.state.addressupdate ? this.addressupdate() : this.status();
  };

  render() {
    const logintype = this.props.logintype + "  Dashboard";
    return (
      <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
        <Container>
          <Navbar.Brand href="#home">Address-Update</Navbar.Brand>
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav style={{ marginLeft: "70%" }}>
              {this.props.logintype === "Requester" ? (
                <Nav.Link onClick={this.enable}>
                  {!this.state.addressupdate ? "Address Update" : "Status"}
                </Nav.Link>
              ) : (
                <div />
              )}
              <Nav.Link style={{ marginLeft: 30 }}>{logintype}</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    );
  }
}

export { LoginNavbar, GenericNavbar };
