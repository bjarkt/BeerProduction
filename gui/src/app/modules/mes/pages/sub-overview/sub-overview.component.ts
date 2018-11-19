import { Component, OnInit } from "@angular/core";
import { Order } from "src/app/shared/models/order";
import { DataService } from "src/app/shared/services/data.service";
import { ProductionInfo } from "src/app/shared/models/ProductionInfo";
import { OrderItem } from "src/app/shared/models/orderItem";
import { Observable } from "rxjs";
@Component({
  selector: "app-sub-overview",
  templateUrl: "./sub-overview.component.html",
  styleUrls: ["./sub-overview.component.scss"]
})
export class SubOverviewComponent implements OnInit {
  
  tabs = [
    {path: './create-batches', label: 'Create Batches'}
  ]
  

  constructor() {}

  ngOnInit() {
    
  }
}
