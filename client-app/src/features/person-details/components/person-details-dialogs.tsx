import { useQueryClient } from '@tanstack/react-query'
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { PersonDetailsDeleteDialog } from './person-details-delete-dialog'
import { usePersonDetailsContext } from '../context/person-details-provider'

export function PersonDetailsDialogs() {
  const queryClient = useQueryClient()
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = usePersonDetailsContext()

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['person-details'] })
  }

  const bulkUploadConfig = {
    entityName: 'PersonDetails',
    uploadEndpoint: '/api/person-details/bulk-upload',
    templateEndpoint: '/api/person-details/download-template',
    exportEndpoint: '/api/person-details/export',
    errorReportEndpoint: '/api/person-details/export-errors',
    onSuccess: handleBulkUploadSuccess,
  }

  return (
    <>
      <PersonDetailsDeleteDialog />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  )
}
