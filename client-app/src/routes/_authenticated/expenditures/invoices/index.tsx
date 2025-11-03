import { createFileRoute } from '@tanstack/react-router'
import ExpendituresInvoicePage from '@/features/expenditures-invoice'

export const Route = createFileRoute('/_authenticated/expenditures/invoices/')({
  component: ExpendituresInvoicePage,
})
