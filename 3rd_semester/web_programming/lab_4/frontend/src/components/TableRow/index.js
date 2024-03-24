const TableRow = (props) => {
    return <tr>
        <td>{props.x}</td>
        <td>{props.y}</td>
        <td>{props.r}</td>
        <td>{(props.result) ? "true" : "false"}</td>
        <td>{props.time}</td>
    </tr>
}

export default TableRow