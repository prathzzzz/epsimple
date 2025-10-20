import { Button } from '@/components/ui/button'
import { Plus } from 'lucide-react'
import { useBanks } from './banks-provider'

export function BanksPrimaryButtons() {
  const { setOpen } = useBanks()

  return (
    <div className='flex items-center gap-2'>
      <Button onClick={() => setOpen('create')} size='sm' className='h-8'>
        <Plus className='mr-2 size-4' />
        Add Bank
      </Button>
    </div>
  )
}
