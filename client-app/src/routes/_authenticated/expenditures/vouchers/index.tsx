import { createFileRoute } from '@tanstack/react-router'
import ExpendituresVoucherPage from '@/features/expenditures-voucher'

export const Route = createFileRoute('/_authenticated/expenditures/vouchers/')({
  component: ExpendituresVoucherPage,
})
