import { Component, OnInit, OnDestroy } from '@angular/core';
import { DataService } from 'src/app/shared/services/data.service';
import { ScadaScreen } from 'src/app/shared/models/scadaScreen';

@Component({
  selector: 'app-screen',
  templateUrl: './screen.component.html',
  styleUrls: ['./screen.component.scss']
})
export class ScreenComponent implements OnInit, OnDestroy {
  timer;
  model: ScadaScreen = {batchID: null, orderNumber: null, beerType: null, produced: null, accepted: null, defective: null, temperature: null, humidity: null, vibration: null, productAmount: null, machineSpeed: null, state: null};
  
  constructor(private data: DataService) { }

  ngOnInit() {
    this.timer = setInterval(() => {
      this.data.viewScreen().subscribe(res => {
        this.updateScreen(res);
      });
    }, 500);
  }

  ngOnDestroy () {
    clearInterval(this.timer);
  }

  updateScreen (res) {
    this.model.state = res["State"];
    
    if (res["BatchInfo"] != null && res["State"] == "EXECUTE") {
      this.model.batchID = res["BatchOrder"]["batchId"];
      this.model.orderNumber = res["BatchInfo"]["orderNumber"];
      this.model.beerType = res["BatchInfo"]["beerName"];
      this.model.produced = res["BatchData"]["produced"];
      this.model.accepted = res["BatchData"]["acceptable"];
      this.model.defective = res["BatchData"]["defect"];
      this.model.temperature = res["Measurements"]["temperature"];
      this.model.humidity = res["Measurements"]["humidity"];
      this.model.vibration = res["Measurements"]["vibration"];
      this.model.productAmount = res["BatchOrder"]["amountToProduce"];
      this.model.machineSpeed = res["BatchOrder"]["productsPerMinute"];
    }
  }
  
  manageProduction (action: string) {
    this.data.manageProduction(action).toPromise();
  }



}
