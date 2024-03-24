import { useNavigate } from 'react-router-dom';
import { useEffect, useState, useRef } from 'react';
import styles from "./index.module.css";
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import TableRow from '../TableRow';
import Table from 'react-bootstrap/Table';


const Main = () => {
    const [x, setX] = useState(0)
    const [y, setY] = useState(0)
    const [r, setR] = useState(1)
    const [graphX, setGraphX] = useState(0)
    const [graphY, setGraphY] = useState(0)
    const [results, setResults] = useState([])
    const token = localStorage.getItem('token')
    const navigate = useNavigate();
    const canvasRef = useRef(null);

    const canvasWidth = 200;
    const canvasHeight = 200;
    const unitX = canvasWidth / 10;
    const unitY = canvasHeight / 10;

    const handleCanvasClick = (event) => { // обрабатываем клик по канвасу
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        const gx = (event.clientX - canvas.getBoundingClientRect().left)
        const gy = (event.clientY - canvas.getBoundingClientRect().top)
        // привести к нормальному виду координаты
        setGraphX(gx) // ВНИМАНИЕ: ФУНКЦИЯ АСИНХРОННАЯ => ЕСЛИ МЫ ИСПОЛЬЗУЕМ graphX/graphY в fetch, то там может быть старое значение 
        setGraphY(gy)
        const token = localStorage.getItem('token')
        const normX = Math.round(gx - canvasWidth / 2) / unitX;
        const normY = Math.round(-1 * (gy - canvasHeight / 2)) / unitY;

        fetch('/addNewResult', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }, body: JSON.stringify({
                x: normX, // тут ПОКА ЧТО отправляются плохие координаты 
                y: normY,
                r: r
            })
        }).then(res => res.json()).then((res) => { // оправляем новый результат, ресуем точку и добавляем его в стейт
            drawDot(getCanvasCoordsFromXY(res.x, res.y), res.isHit)
            setResults(results => [...results, res])
        })
    }

    const drawDot = (XY, r) => {
        const x = XY[0];
        const y = XY[1];
        // отрисовка точек
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        ctx.beginPath();
        ctx.arc(x, y, 4, 0, 2 * Math.PI);
        ctx.fillStyle = (r) ? 'black' : 'white'
        ctx.lineWidth = 1;
        ctx.strokeStyle = 'black';
        ctx.fill();
        ctx.stroke()
        ctx.closePath();
    }

    const drawAxes = () => {
        // очистка точек + отрисовка осей
        // МОЖНО ТУТ РИСОВАТЬ ОБЛАСТЬ !!!!!!
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');

        ctx.beginPath()
        ctx.clearRect(0, 0, canvas.clientWidth, canvas.clientHeight); // очистка всего

        ctx.fillStyle = 'gray';
        ctx.strokeStyle = 'gray';
        ctx.fillRect(canvasWidth / 2 - r * unitX, canvasHeight / 2 - r * unitY, r * unitX, r * unitY);

        ctx.beginPath();
        if (r >= 0) {
            ctx.arc(canvasWidth / 2, canvasHeight / 2, r * unitX, 0.5 * Math.PI, Math.PI, false);
        } else {
            ctx.arc(canvasWidth / 2, canvasHeight / 2, -1 * r * unitX, 1.5 * Math.PI, 2 * Math.PI, false);
        }
        ctx.closePath();
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(canvasWidth / 2, canvasHeight / 2 + r * unitX);
        ctx.lineTo(canvasWidth / 2, canvasHeight / 2);
        ctx.lineTo(canvasWidth / 2 - r * unitX, canvasHeight / 2);
        ctx.closePath();
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(canvasWidth / 2, canvasHeight / 2);
        ctx.lineTo(canvasWidth / 2 + r * unitX, canvasHeight / 2);
        ctx.lineTo(canvasWidth / 2, canvasHeight / 2 - r * unitY / 2);
        ctx.closePath();
        ctx.fill();

        ctx.strokeStyle = 'black';
        ctx.moveTo(0, canvas.clientHeight / 2);
        ctx.lineTo(canvas.clientWidth, canvas.clientHeight / 2);
        ctx.moveTo(canvas.clientWidth / 2, 0);
        ctx.lineTo(canvas.clientWidth / 2, canvas.clientHeight);
        ctx.stroke();
    };

    const getCanvasCoordsFromXY = (x, y) => {
        const coordX = unitX * x + canvasWidth / 2;
        const coordY = canvasHeight / 2 - unitY * y;
        return [coordX, coordY];
    }

    const loadResults = () => {
        setResults([])
        fetch('/getAllUserResults', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => res.json()).then((res) => { // грузим результаты
            drawAxes() // очищаем график
            for (const i of res) { // рисуем только точки с установленным радиусом
                if (i.r === r) {
                    drawDot(getCanvasCoordsFromXY(i.x, i.y), i.isHit)
                }
                setResults(results => [...results, i]) // добавляем каждую точку в стейт
            }

        })
    }

    const handleCheck = () => { // обрабатываем клик по кнопке
        const token = localStorage.getItem('token')
        fetch('/addNewResult', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }, body: JSON.stringify({
                x: x,
                y: y,
                r: r
            })
        }).then(res => res.json()).then((res) => {
            setResults(results => [...results, res]) // добавляем в стейт результат 
            drawDot(getCanvasCoordsFromXY(res.x, res.y), res.isHit) // отрисовываем новую точку
        })
    }

    useEffect(() => { // при рендере компонента, проверяем залогиненность
        const token = localStorage.getItem('token')
        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then((res) => {
            if (res.status !== 200) {
                navigate('/')
            }
        })
        loadResults() // подгружаем результаты
    }, [])

    useEffect(() => {
        drawAxes()
        for (const i of results) { // рисуем точки с нашим Р
            if (i.r === r) {
                drawDot(getCanvasCoordsFromXY(i.x, i.y), i.isHit)
            }
        }
    }, [r])

    const handleChangeR = (val) => { // обрабатываем изменение Р
        setR(val) // устанавливаем стейт Р
        // drawAxes() // очищаем график

        // for (const i of results) { // рисуем точки с нашим Р
        //     if (i.r === val) {
        //         drawDot(getCanvasCoordsFromXY(i.x, i.y), i.isHit)
        //     }
        // }
    }

    // const renderGraph = () => { // тут ничего толком нет пока что, инициализируем график
    //     // сюда можно перенести отрисовку осей и фигур
    //     const canvas = canvasRef.current;
    //     const ctx = canvas.getContext('2d');

    //     const centerX = canvas.clientWidth / 2
    //     const centerY = canvas.clientHeight / 2

    //     ctx.beginPath()
    //     ctx.clearRect(0, 0, canvas.clientWidth, canvas.clientHeight);

    // }

    return <div className='App'>
        <div className='wrapper'> 
            <canvas width={canvasWidth} height={canvasHeight} ref={canvasRef} onClick={handleCanvasClick}></canvas>

        {/* debug info: (x:{x}, y:{y}, r:{r}, graphX: {graphX}, graphY: {graphY}) */}
        <div className='r'>
            x:
            <Button variant="secondary" onClick={() => {
                setX(-5)
            }}>-5</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(-4)
            }}>-4</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(-3)
            }}>-3</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(-2)
            }}>-2</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(-1)
            }}>-1</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(0)
            }}>0</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(1)
            }}>1</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(2)
            }}>2</Button>{' '}
            <Button variant="secondary" onClick={() => {
                setX(3)
            }}>3</Button>{' '}
        </div>

        <div className='r'>
        r:
        <Button variant="danger" onClick={() => {
            handleChangeR(-5)
        }}>-5</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(-4)
        }}>-4</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(-3)
        }}>-3</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(-2)
        }}>-2</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(-1)
        }}>-1</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(0)
        }}>0</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(1)
        }}>1</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(2)
        }}>2</Button>{' '}
        <Button variant="danger" onClick={() => {
            handleChangeR(3)
        }}>3</Button>{' '}
        </div>
        
        <div className='r' style={{justifyContent: 'space-around'}}>
            <input style={{
                width: "100px"
            }} type='number' min='-5' max='5'value={y} onChange={(event) => {
                if (Number(event.target.value) > 5) {
                    setY(5)
                } else if (Number(event.target.value) < -5) {
                    setY(-5)
                } else {
                    setY(event.target.value)
                }
                
            }} placeholder='y' />
            <Button onClick={handleCheck}>Check</Button>{' '}
            
        </div>

        <div className='r' style={{justifyContent: 'space-around'}}>
        <Button variant="warning" onClick={() => {
            localStorage.clear()
            navigate('/')
            }}>Log out</Button>{' '}

            <Button variant="dark" onClick={() => {
                fetch('/deleteAllUserResults', {
                    method: 'GET',
                    headers: {
                        'Authorization': localStorage.getItem('token')
                    }
                }).then(() => {
                    setResults([])
                    drawAxes()
                })

            }}>Drop table</Button>{' '}
        </div>

        <div style={{
            maxHeight: "200px",
            minHeight: "200px",
            overflow: "scroll"
        }}>
        <Table responsive striped bordered hover>
            <thead>
                <tr>
                    <td>x</td>
                    <td>y</td>
                    <td>r</td>
                    <td>isHit</td>
                    <td>CurrentTime</td>
                </tr>
            </thead>
            <tbody>
                {results.map((e) => {
                    return <TableRow x={e.x} y={e.y} r={e.r} result={e.isHit} time={e.dateAndTime}/>
                })}
            </tbody>
        </Table>
        </div>
    </div>
</div>
}

export default Main