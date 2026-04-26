import { Link } from "react-router";
import { Navbar, Container } from "react-bootstrap";
import { AdminNavMenu } from "../components/adminNavBar/AdminNavMenu";
import { AdminNavActions } from "../components/adminNavBar/AdminNavActions";

export const AdminHeader = () => {
    return (
        <Navbar expand="lg">
            <Container fluid>
                <Navbar.Brand as={Link} to="/admin">
                    TicketHub Admin
                </Navbar.Brand>

                <Navbar.Toggle aria-controls="adminNavbarContent" />

                <Navbar.Collapse id="adminNavbarContent">
                    <AdminNavMenu />
                    <AdminNavActions />
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};
