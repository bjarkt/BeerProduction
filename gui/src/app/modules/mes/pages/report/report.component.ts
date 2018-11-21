import { Component, OnInit } from '@angular/core';
import { Batch } from 'src/app/shared/models/Batch';
import { MesDataService } from 'src/app/shared/services/mesData.service';
import { OEE } from 'src/app/shared/models/OEE';


@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {
  oee: OEE; 
  batches: Batch[] = [];
  selectedBatch: Batch = {started: null, orderNumber: null, machineSpeed: null, 
    finished: null, defect: null, beerName: null, accepted: null, batchId: null}

  constructor(private data: MesDataService) { }

  ngOnInit() {
    this.data.getBatches().subscribe(result => {
      this.batches = result
      this.batches.sort((a,b) => {
          if(a.batchId > b.batchId) {
            return 1
          } else if(a.batchId < b.batchId) {
            return -1
          } else {
            return 0
          }
      })

    })
  }

  public getReport() {
    this.data.getReport(this.selectedBatch.batchId).subscribe(pdfData => {
      this.data.downloadFile(pdfData, "application/pdf")
    })
  }


  public getOEE() {
    this.data.getOEE(this.selectedBatch.batchId).subscribe(result => {
      this.oee = result
    })
  }

}
