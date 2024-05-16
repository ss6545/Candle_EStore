import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandleSearchComponent } from './candle-search.component';

describe('CandleSearchComponent', () => {
  let component: CandleSearchComponent;
  let fixture: ComponentFixture<CandleSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CandleSearchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CandleSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
