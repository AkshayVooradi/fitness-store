import { Carousel } from "bootstrap";
import "./App.css";
import Navbar from "./customer/components/Navigation/Navbar";
import Homepage from "./pages/Homepage/Homepage";

function App() {
  return (
    <div className="App">
      <Navbar></Navbar>
      <div>
        <Homepage></Homepage>
      </div>
    </div>
  );
}

export default App;
