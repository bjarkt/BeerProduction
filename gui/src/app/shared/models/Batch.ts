import { LocalDateTime } from "./localdatetime";

export interface Batch {
    beerName: string;
    orderNumber: number;
    batchId: number;
    started: LocalDateTime;
    finished: LocalDateTime;
    accepted: number;
    defect: number;
    machineSpeed: number;
    
}