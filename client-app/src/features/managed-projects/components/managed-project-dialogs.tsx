import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { useManagedProjectContext } from '../context/managed-project-provider'

export function ManagedProjectDialogs() {
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = useManagedProjectContext()

  return (
    <>
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={(open: boolean) => {
          if (!open) closeBulkUploadDialog()
        }}
        config={{
          entityName: 'ManagedProject',
          uploadEndpoint: '/api/managed-projects/bulk/upload',
          errorReportEndpoint: '/api/managed-projects/bulk/export-error-report',
        }}
      />
    </>
  )
}
