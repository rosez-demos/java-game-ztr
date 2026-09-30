let gameId = null;

const boardEl = document.getElementById('board');
const statusEl = document.getElementById('status');
const newGameBtn = document.getElementById('new-game');

async function startNewGame() {
  const res = await fetch('/api/game/new', { method: 'POST' });
  const state = await res.json();
  gameId = state.id;
  render(state);
}

async function playMove(row, col) {
  const res = await fetch(`/api/game/${gameId}/move`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ row, col }),
  });
  if (!res.ok) {
    return; // occupied cell or finished game: no-op
  }
  render(await res.json());
}

function render(state) {
  boardEl.innerHTML = '';
  const winCells = new Set((state.winningLine || []).map(([r, c]) => `${r}-${c}`));

  for (let r = 0; r < 3; r++) {
    for (let c = 0; c < 3; c++) {
      const mark = state.board[r][c];
      const cell = document.createElement('div');
      cell.className = 'cell';
      if (mark) {
        cell.classList.add('taken', mark.toLowerCase());
      }
      if (winCells.has(`${r}-${c}`)) {
        cell.classList.add('win');
      }
      cell.textContent = mark;

      const finished = state.status !== 'IN_PROGRESS';
      if (!mark && !finished) {
        cell.addEventListener('click', () => playMove(r, c));
      }
      boardEl.appendChild(cell);
    }
  }

  statusEl.textContent = statusText(state);
}

function statusText(state) {
  switch (state.status) {
    case 'X_WON':
      return 'X wins!';
    case 'O_WON':
      return 'O wins!';
    case 'DRAW':
      return "It's a draw!";
    default:
      return `${state.currentPlayer}'s turn`;
  }
}

newGameBtn.addEventListener('click', startNewGame);

startNewGame();
