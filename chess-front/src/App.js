import "./App.css";
import { Chessboard } from "react-chessboard";
import axios from "axios";
import { useState } from "react";
import React from "react";

function App() {
  const [boardState, setBoardState] = useState({});
  const [lastClickedPieceData, setLastClickedPieceData] = useState({});

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

  const onPromotionPieceSelect = (promotedPiece) => {
    console.log("onPromotionPieceSelect", promotedPiece);

    const newPiece = promotedPiece[1];

    axios
      .post("http://localhost:8180/chess/move", {
        from: lastClickedPieceData.sourceSquare,
        to: lastClickedPieceData.targetSquare,
        additionalProperties: {
          promoteTo: newPiece,
        },
      })
      .then((response) => {
        console.log("Move response:", response.data);
        setBoardState(response.data);
      })
      .catch((error) => {
        console.log("Move error:", error.message);
      });
  };

  const onPromotionCheck = (sourceSquare, targetSquare, piece) => {
    setLastClickedPieceData({
      sourceSquare,
      targetSquare,
      piece,
    });

    console.log("On Promotion Check", sourceSquare, targetSquare, piece);

    if (targetSquare[1] != 8 && targetSquare[1] != 1) {
      console.log("Returned false 1");
      return false;
    }
    if (piece[1].toLowerCase() != "p") {
      console.log("Returned false 2", piece.toLowerCase());
      return false;
    }
    console.log("Returned true");
    return true;
  };

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
        onPieceDrop={onPieceDrop}
        onPromotionPieceSelect={onPromotionPieceSelect}
        onPromotionCheck={onPromotionCheck}
        id="BasicBoard"
      />
    </div>
  );
}

export default App;
