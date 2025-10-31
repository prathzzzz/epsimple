import { format } from 'date-fns'
import { Calendar as CalendarIcon } from 'lucide-react'
import { cn } from '@/lib/utils'
import { Button } from '@/components/ui/button'
import { Calendar } from '@/components/ui/calendar'
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover'

type DatePickerProps = {
  selected: Date | undefined
  onSelect: (date: Date | undefined) => void
  placeholder?: string
  disabled?: boolean
  disableFutureDates?: boolean
  disablePastDates?: boolean
  fromDate?: Date
  toDate?: Date
  className?: string
}

export function DatePicker({
  selected,
  onSelect,
  placeholder = 'Pick a date',
  disabled = false,
  disableFutureDates = false,
  disablePastDates = false,
  fromDate,
  toDate,
  className,
}: DatePickerProps) {
  const getDisabledDates = (date: Date) => {
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    
    if (disableFutureDates && date > today) {
      return true
    }
    
    if (disablePastDates && date < today) {
      return true
    }
    
    if (fromDate && date < fromDate) {
      return true
    }
    
    if (toDate && date > toDate) {
      return true
    }
    
    return false
  }

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant='outline'
          disabled={disabled}
          className={cn(
            'w-full justify-start text-left font-normal',
            !selected && 'text-muted-foreground',
            className
          )}
        >
          <CalendarIcon className='mr-2 h-4 w-4' />
          {selected ? format(selected, 'PPP') : <span>{placeholder}</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className='w-auto p-0' align='start'>
        <Calendar
          mode='single'
          captionLayout='dropdown'
          selected={selected}
          onSelect={onSelect}
          disabled={getDisabledDates}
          fromYear={1900}
          toYear={new Date().getFullYear() + 10}
          initialFocus
        />
      </PopoverContent>
    </Popover>
  )
}
