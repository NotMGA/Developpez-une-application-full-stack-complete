import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
})
export class FormComponent {
  /**
   * The reactive form group for managing form state.
   */
  @Input() formGroup!: FormGroup;

  /**
   * The array of fields to be rendered in the form.
   */
  @Input() fields: Array<{
    name: string;
    label: string;
    type: string;
    validators?: any[];
  }> = [];

  /**
   * Event emitter for form submission.
   */
  @Output() formSubmit = new EventEmitter<any>();

  /**
   * Emits the form data when the form is valid and submitted.
   */
  onSubmit(): void {
    if (this.formGroup.valid) {
      this.formSubmit.emit(this.formGroup.value);
    }
  }
}
