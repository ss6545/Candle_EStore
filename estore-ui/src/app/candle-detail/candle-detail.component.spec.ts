import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandleDetailComponent } from './candle-detail.component';

describe('CandleDetailComponent', () => {
  let component: CandleDetailComponent;
  let fixture: ComponentFixture<CandleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CandleDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CandleDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
