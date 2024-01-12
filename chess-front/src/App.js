import logo from "./logo.svg";
import "./App.css";
import { Chessboard } from "react-chessboard";
import axios from "axios";
import { useState } from "react";

function App() {
  const [boardState, setBoardState] = useState({});

  useState(() => {
    axios
      .post("http://localhost:8180/chess/initialize")
      .then((response) => {
        setBoardState(response.data);
      })
      .catch((error) => {
        console.log({ error });
      });
  }, []);

  const onPieceDrop = (sourceSquare, targetSquare, piece) => {
    const player = piece[0];

    console.log("onPieceDrop:", sourceSquare, targetSquare, piece[1], player);

    axios
      .post("http://localhost:8180/chess/move", {
        from: sourceSquare,
        to: targetSquare,
      })
      .then((response) => {
        console.log("Move response:", response.data);
        setBoardState(response.data);
      })
      .catch((error) => {
        console.log("Move error:", error.message);
      });

    return true;
  };

  return (
    <div className="App">
      <Chessboard
        position={boardState}
        onPieceDrop={(sourceSquare, targetSquare, piece) =>
          onPieceDrop(sourceSquare, targetSquare, piece)
        }
        id="BasicBoard"
      />
    </div>
  );
}

export default App;
