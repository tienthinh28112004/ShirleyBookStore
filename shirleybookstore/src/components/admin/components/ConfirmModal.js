import {Button, Modal} from "react-bootstrap";

const ConfirmModal = ({show,onHide,onConfirm,itemName}) =>{
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>Remove Confirmation</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div>Are you sure you want to remove '{itemName}' from the TEACHER role?</div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Cancel</Button>
                <Button variant="danger" onClick={onConfirm}>Delete</Button>
            </Modal.Footer>
        </Modal>
    )
}